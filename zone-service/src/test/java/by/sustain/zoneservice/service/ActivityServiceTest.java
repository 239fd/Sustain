package by.sustain.zoneservice.service;

import by.sustain.zoneservice.dto.activity.ActivityListResponse;
import by.sustain.zoneservice.dto.activity.ActivityResponse;
import by.sustain.zoneservice.dto.activity.CreateActivityRequest;
import by.sustain.zoneservice.dto.activity.UpdateActivityRequest;
import by.sustain.zoneservice.entity.Activity;
import by.sustain.zoneservice.entity.Area;
import by.sustain.zoneservice.entity.enums.EnergyLevel;
import by.sustain.zoneservice.exception.ResourceAlreadyExistsException;
import by.sustain.zoneservice.exception.ResourceNotFoundException;
import by.sustain.zoneservice.mapper.ActivityMapper;
import by.sustain.zoneservice.repository.ActivityRepository;
import by.sustain.zoneservice.repository.AreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActivityService Tests")
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private ActivityMapper activityMapper;

    @InjectMocks
    private ActivityService activityService;

    private UUID userId;
    private UUID areaId;
    private UUID activityId;
    private Area area;
    private Activity activity;
    private ActivityResponse activityResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        areaId = UUID.randomUUID();
        activityId = UUID.randomUUID();

        area = new Area();
        area.setId(areaId);
        area.setUserId(userId);
        area.setName("Strategist");
        area.setColor("#FFAA00");
        area.setEnergy(EnergyLevel.HIGH);
        area.setBlocked(false);

        activity = new Activity();
        activity.setId(activityId);
        activity.setName("Quarter Planning");
        activity.setArea(area);

        activityResponse = new ActivityResponse(
                activityId,
                areaId,
                "Quarter Planning"
        );
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create activity successfully when valid request provided")
        void create_WithValidRequest_ShouldReturnActivityResponse() {
            // Arrange
            CreateActivityRequest request = new CreateActivityRequest(
                    areaId,
                    "Quarter Planning"
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(activityRepository.existsByAreaIdAndName(areaId, "Quarter Planning")).thenReturn(false);
            when(activityRepository.save(any(Activity.class))).thenReturn(activity);
            when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

            // Act
            ActivityResponse result = activityService.create(request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(activityId);
            assertThat(result.areaId()).isEqualTo(areaId);
            assertThat(result.name()).isEqualTo("Quarter Planning");

            verify(areaRepository).findById(areaId);
            verify(activityRepository).existsByAreaIdAndName(areaId, "Quarter Planning");
            verify(activityRepository).save(any(Activity.class));
            verify(activityMapper).toResponse(activity);
        }

        @Test
        @DisplayName("Should save activity with correct area reference")
        void create_WithValidRequest_ShouldSetAreaCorrectly() {
            // Arrange
            CreateActivityRequest request = new CreateActivityRequest(
                    areaId,
                    "OKR Planning"
            );

            ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);
            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(activityRepository.existsByAreaIdAndName(areaId, "OKR Planning")).thenReturn(false);
            when(activityRepository.save(activityCaptor.capture())).thenReturn(activity);
            when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

            // Act
            activityService.create(request);

            // Assert
            Activity capturedActivity = activityCaptor.getValue();
            assertThat(capturedActivity.getArea()).isEqualTo(area);
            assertThat(capturedActivity.getName()).isEqualTo("OKR Planning");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when area not found")
        void create_WithNonExistingArea_ShouldThrowException() {
            // Arrange
            CreateActivityRequest request = new CreateActivityRequest(
                    areaId,
                    "Quarter Planning"
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> activityService.create(request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(areaRepository).findById(areaId);
            verify(activityRepository, never()).save(any(Activity.class));
        }

        @Test
        @DisplayName("Should throw ResourceAlreadyExistsException when activity with same name exists in area")
        void create_WithDuplicateName_ShouldThrowException() {
            // Arrange
            CreateActivityRequest request = new CreateActivityRequest(
                    areaId,
                    "Quarter Planning"
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(activityRepository.existsByAreaIdAndName(areaId, "Quarter Planning")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> activityService.create(request))
                    .isInstanceOf(ResourceAlreadyExistsException.class);

            verify(areaRepository).findById(areaId);
            verify(activityRepository).existsByAreaIdAndName(areaId, "Quarter Planning");
            verify(activityRepository, never()).save(any(Activity.class));
        }
    }

    @Nested
    @DisplayName("getAllByAreaId() method tests")
    class GetAllByAreaIdTests {

        @Test
        @DisplayName("Should return list of activities for area")
        void getAllByAreaId_WithExistingActivities_ShouldReturnList() {
            // Arrange
            Activity activity2 = new Activity();
            activity2.setId(UUID.randomUUID());
            activity2.setName("Results Analysis");
            activity2.setArea(area);

            ActivityResponse activityResponse2 = new ActivityResponse(
                    activity2.getId(),
                    areaId,
                    "Results Analysis"
            );

            when(areaRepository.existsById(areaId)).thenReturn(true);
            when(activityRepository.findAllByAreaId(areaId)).thenReturn(List.of(activity, activity2));
            when(activityMapper.toResponse(activity)).thenReturn(activityResponse);
            when(activityMapper.toResponse(activity2)).thenReturn(activityResponse2);

            // Act
            ActivityListResponse result = activityService.getAllByAreaId(areaId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.activities()).hasSize(2);
            assertThat(result.activities()).extracting(ActivityResponse::name)
                    .containsExactlyInAnyOrder("Quarter Planning", "Results Analysis");

            verify(areaRepository).existsById(areaId);
            verify(activityRepository).findAllByAreaId(areaId);
        }

        @Test
        @DisplayName("Should return empty list when area has no activities")
        void getAllByAreaId_WithNoActivities_ShouldReturnEmptyList() {
            // Arrange
            when(areaRepository.existsById(areaId)).thenReturn(true);
            when(activityRepository.findAllByAreaId(areaId)).thenReturn(List.of());

            // Act
            ActivityListResponse result = activityService.getAllByAreaId(areaId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.activities()).isEmpty();

            verify(areaRepository).existsById(areaId);
            verify(activityRepository).findAllByAreaId(areaId);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when area not found")
        void getAllByAreaId_WithNonExistingArea_ShouldThrowException() {
            // Arrange
            when(areaRepository.existsById(areaId)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> activityService.getAllByAreaId(areaId))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(areaRepository).existsById(areaId);
            verify(activityRepository, never()).findAllByAreaId(any());
        }
    }

    @Nested
    @DisplayName("update() method tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update activity successfully when valid request provided")
        void update_WithValidRequest_ShouldReturnUpdatedActivity() {
            // Arrange
            UpdateActivityRequest request = new UpdateActivityRequest("OKR Planning");

            ActivityResponse updatedResponse = new ActivityResponse(
                    activityId,
                    areaId,
                    "OKR Planning"
            );

            when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
            when(activityRepository.existsByAreaIdAndNameAndIdNot(areaId, "OKR Planning", activityId)).thenReturn(false);
            when(activityRepository.save(any(Activity.class))).thenReturn(activity);
            when(activityMapper.toResponse(activity)).thenReturn(updatedResponse);

            // Act
            ActivityResponse result = activityService.update(activityId, request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("OKR Planning");

            verify(activityRepository).findById(activityId);
            verify(activityRepository).save(activity);
        }

        @Test
        @DisplayName("Should update activity name correctly")
        void update_WithNewName_ShouldUpdateName() {
            // Arrange
            UpdateActivityRequest request = new UpdateActivityRequest("Weekly Review");

            when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
            when(activityRepository.existsByAreaIdAndNameAndIdNot(areaId, "Weekly Review", activityId)).thenReturn(false);
            when(activityRepository.save(any(Activity.class))).thenReturn(activity);
            when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

            // Act
            activityService.update(activityId, request);

            // Assert
            assertThat(activity.getName()).isEqualTo("Weekly Review");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when activity not found")
        void update_WithNonExistingActivity_ShouldThrowException() {
            // Arrange
            UpdateActivityRequest request = new UpdateActivityRequest("OKR Planning");

            when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> activityService.update(activityId, request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(activityRepository).findById(activityId);
            verify(activityRepository, never()).save(any(Activity.class));
        }

        @Test
        @DisplayName("Should throw ResourceAlreadyExistsException when new name already exists in area")
        void update_WithDuplicateName_ShouldThrowException() {
            // Arrange
            UpdateActivityRequest request = new UpdateActivityRequest("Results Analysis");

            when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
            when(activityRepository.existsByAreaIdAndNameAndIdNot(areaId, "Results Analysis", activityId)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> activityService.update(activityId, request))
                    .isInstanceOf(ResourceAlreadyExistsException.class);

            verify(activityRepository).findById(activityId);
            verify(activityRepository, never()).save(any(Activity.class));
        }

        @Test
        @DisplayName("Should not check name uniqueness when name unchanged")
        void update_WithSameName_ShouldNotCheckUniqueness() {
            // Arrange
            UpdateActivityRequest request = new UpdateActivityRequest("Quarter Planning"); // same name

            when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));
            when(activityRepository.save(any(Activity.class))).thenReturn(activity);
            when(activityMapper.toResponse(activity)).thenReturn(activityResponse);

            // Act
            activityService.update(activityId, request);

            // Assert
            verify(activityRepository, never()).existsByAreaIdAndNameAndIdNot(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("delete() method tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete activity successfully when exists")
        void delete_WithExistingActivity_ShouldDeleteSuccessfully() {
            // Arrange
            when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));

            // Act
            activityService.delete(activityId);

            // Assert
            verify(activityRepository).findById(activityId);
            verify(activityRepository).delete(activity);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when activity not found")
        void delete_WithNonExistingActivity_ShouldThrowException() {
            // Arrange
            when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> activityService.delete(activityId))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(activityRepository).findById(activityId);
            verify(activityRepository, never()).delete(any(Activity.class));
        }
    }
}
