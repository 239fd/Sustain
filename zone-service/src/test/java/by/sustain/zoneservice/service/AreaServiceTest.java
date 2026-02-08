package by.sustain.zoneservice.service;

import by.sustain.zoneservice.dto.area.AreaListResponse;
import by.sustain.zoneservice.dto.area.AreaResponse;
import by.sustain.zoneservice.dto.area.CreateAreaRequest;
import by.sustain.zoneservice.dto.area.UpdateAreaRequest;
import by.sustain.zoneservice.entity.Area;
import by.sustain.zoneservice.entity.enums.EnergyLevel;
import by.sustain.zoneservice.exception.AccessDeniedException;
import by.sustain.zoneservice.exception.ResourceAlreadyExistsException;
import by.sustain.zoneservice.exception.ResourceNotFoundException;
import by.sustain.zoneservice.mapper.AreaMapper;
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
@DisplayName("AreaService Tests")
class AreaServiceTest {

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private AreaMapper areaMapper;

    @InjectMocks
    private AreaService areaService;

    private UUID userId;
    private UUID areaId;
    private Area area;
    private AreaResponse areaResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        areaId = UUID.randomUUID();

        area = new Area();
        area.setId(areaId);
        area.setUserId(userId);
        area.setName("Strategist");
        area.setColor("#FFAA00");
        area.setEnergy(EnergyLevel.HIGH);
        area.setBlocked(false);

        areaResponse = new AreaResponse(
                areaId,
                userId,
                "Strategist",
                "#FFAA00",
                "HIGH",
                false
        );
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create area successfully when valid request provided")
        void create_WithValidRequest_ShouldReturnAreaResponse() {
            // Arrange
            CreateAreaRequest request = new CreateAreaRequest(
                    userId,
                    "Strategist",
                    "#FFAA00",
                    "HIGH",
                    false
            );

            when(areaRepository.existsByUserIdAndName(userId, "Strategist")).thenReturn(false);
            when(areaRepository.save(any(Area.class))).thenReturn(area);
            when(areaMapper.toResponse(area)).thenReturn(areaResponse);

            // Act
            AreaResponse result = areaService.create(request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(areaId);
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.name()).isEqualTo("Strategist");
            assertThat(result.color()).isEqualTo("#FFAA00");
            assertThat(result.energy()).isEqualTo("HIGH");
            assertThat(result.isBlocked()).isFalse();

            verify(areaRepository).existsByUserIdAndName(userId, "Strategist");
            verify(areaRepository).save(any(Area.class));
            verify(areaMapper).toResponse(area);
        }

        @Test
        @DisplayName("Should create area with default isBlocked=false when not provided")
        void create_WithNullIsBlocked_ShouldDefaultToFalse() {
            // Arrange
            CreateAreaRequest request = new CreateAreaRequest(
                    userId,
                    "Health",
                    "#00FF00",
                    "MEDIUM",
                    null
            );

            ArgumentCaptor<Area> areaCaptor = ArgumentCaptor.forClass(Area.class);
            when(areaRepository.existsByUserIdAndName(userId, "Health")).thenReturn(false);
            when(areaRepository.save(areaCaptor.capture())).thenReturn(area);
            when(areaMapper.toResponse(area)).thenReturn(areaResponse);

            // Act
            areaService.create(request);

            // Assert
            Area capturedArea = areaCaptor.getValue();
            assertThat(capturedArea.isBlocked()).isFalse();
        }

        @Test
        @DisplayName("Should throw ResourceAlreadyExistsException when area with same name exists")
        void create_WithDuplicateName_ShouldThrowException() {
            // Arrange
            CreateAreaRequest request = new CreateAreaRequest(
                    userId,
                    "Strategist",
                    "#FFAA00",
                    "HIGH",
                    false
            );

            when(areaRepository.existsByUserIdAndName(userId, "Strategist")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> areaService.create(request))
                    .isInstanceOf(ResourceAlreadyExistsException.class);

            verify(areaRepository).existsByUserIdAndName(userId, "Strategist");
            verify(areaRepository, never()).save(any(Area.class));
        }

        @Test
        @DisplayName("Should save area with correct energy level")
        void create_WithDifferentEnergyLevels_ShouldSaveCorrectly() {
            // Arrange
            CreateAreaRequest request = new CreateAreaRequest(
                    userId,
                    "Low Energy Task",
                    "#0000FF",
                    "LOW",
                    false
            );

            ArgumentCaptor<Area> areaCaptor = ArgumentCaptor.forClass(Area.class);
            when(areaRepository.existsByUserIdAndName(userId, "Low Energy Task")).thenReturn(false);
            when(areaRepository.save(areaCaptor.capture())).thenReturn(area);
            when(areaMapper.toResponse(area)).thenReturn(areaResponse);

            // Act
            areaService.create(request);

            // Assert
            Area capturedArea = areaCaptor.getValue();
            assertThat(capturedArea.getEnergy()).isEqualTo(EnergyLevel.LOW);
        }
    }

    @Nested
    @DisplayName("getAllByUserId() method tests")
    class GetAllByUserIdTests {

        @Test
        @DisplayName("Should return list of areas for user")
        void getAllByUserId_WithExistingAreas_ShouldReturnList() {
            // Arrange
            Area area2 = new Area();
            area2.setId(UUID.randomUUID());
            area2.setUserId(userId);
            area2.setName("Health");
            area2.setColor("#00FF00");
            area2.setEnergy(EnergyLevel.MEDIUM);
            area2.setBlocked(false);

            AreaResponse areaResponse2 = new AreaResponse(
                    area2.getId(),
                    userId,
                    "Health",
                    "#00FF00",
                    "MEDIUM",
                    false
            );

            when(areaRepository.findAllByUserId(userId)).thenReturn(List.of(area, area2));
            when(areaMapper.toResponse(area)).thenReturn(areaResponse);
            when(areaMapper.toResponse(area2)).thenReturn(areaResponse2);

            // Act
            AreaListResponse result = areaService.getAllByUserId(userId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.areas()).hasSize(2);
            assertThat(result.areas()).extracting(AreaResponse::name)
                    .containsExactlyInAnyOrder("Strategist", "Health");

            verify(areaRepository).findAllByUserId(userId);
        }

        @Test
        @DisplayName("Should return empty list when user has no areas")
        void getAllByUserId_WithNoAreas_ShouldReturnEmptyList() {
            // Arrange
            when(areaRepository.findAllByUserId(userId)).thenReturn(List.of());

            // Act
            AreaListResponse result = areaService.getAllByUserId(userId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.areas()).isEmpty();

            verify(areaRepository).findAllByUserId(userId);
        }
    }

    @Nested
    @DisplayName("update() method tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update area successfully when valid request provided")
        void update_WithValidRequest_ShouldReturnUpdatedArea() {
            // Arrange
            UpdateAreaRequest request = new UpdateAreaRequest(
                    userId,
                    "Strategy",
                    "#00AAFF",
                    "MEDIUM",
                    true
            );

            AreaResponse updatedResponse = new AreaResponse(
                    areaId,
                    userId,
                    "Strategy",
                    "#00AAFF",
                    "MEDIUM",
                    true
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(areaRepository.existsByUserIdAndNameAndIdNot(userId, "Strategy", areaId)).thenReturn(false);
            when(areaRepository.save(any(Area.class))).thenReturn(area);
            when(areaMapper.toResponse(area)).thenReturn(updatedResponse);

            // Act
            AreaResponse result = areaService.update(areaId, request);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("Strategy");
            assertThat(result.color()).isEqualTo("#00AAFF");
            assertThat(result.energy()).isEqualTo("MEDIUM");
            assertThat(result.isBlocked()).isTrue();

            verify(areaRepository).findById(areaId);
            verify(areaRepository).save(area);
        }

        @Test
        @DisplayName("Should update only provided fields")
        void update_WithPartialRequest_ShouldUpdateOnlyProvidedFields() {
            // Arrange
            UpdateAreaRequest request = new UpdateAreaRequest(
                    userId,
                    null,
                    "#00AAFF",
                    null,
                    null
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(areaRepository.save(any(Area.class))).thenReturn(area);
            when(areaMapper.toResponse(area)).thenReturn(areaResponse);

            // Act
            areaService.update(areaId, request);

            // Assert
            assertThat(area.getName()).isEqualTo("Strategist"); // unchanged
            assertThat(area.getColor()).isEqualTo("#00AAFF"); // changed
            assertThat(area.getEnergy()).isEqualTo(EnergyLevel.HIGH); // unchanged
            assertThat(area.isBlocked()).isFalse(); // unchanged
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when area not found")
        void update_WithNonExistingArea_ShouldThrowException() {
            // Arrange
            UpdateAreaRequest request = new UpdateAreaRequest(
                    userId,
                    "Strategy",
                    "#00AAFF",
                    "MEDIUM",
                    true
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> areaService.update(areaId, request))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(areaRepository).findById(areaId);
            verify(areaRepository, never()).save(any(Area.class));
        }

        @Test
        @DisplayName("Should throw AccessDeniedException when user is not owner")
        void update_WithDifferentUserId_ShouldThrowAccessDeniedException() {
            // Arrange
            UUID differentUserId = UUID.randomUUID();
            UpdateAreaRequest request = new UpdateAreaRequest(
                    differentUserId,
                    "Strategy",
                    "#00AAFF",
                    "MEDIUM",
                    true
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));

            // Act & Assert
            assertThatThrownBy(() -> areaService.update(areaId, request))
                    .isInstanceOf(AccessDeniedException.class);

            verify(areaRepository).findById(areaId);
            verify(areaRepository, never()).save(any(Area.class));
        }

        @Test
        @DisplayName("Should throw ResourceAlreadyExistsException when new name already exists")
        void update_WithDuplicateName_ShouldThrowException() {
            // Arrange
            UpdateAreaRequest request = new UpdateAreaRequest(
                    userId,
                    "Health",
                    "#00AAFF",
                    "MEDIUM",
                    true
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(areaRepository.existsByUserIdAndNameAndIdNot(userId, "Health", areaId)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> areaService.update(areaId, request))
                    .isInstanceOf(ResourceAlreadyExistsException.class);

            verify(areaRepository).findById(areaId);
            verify(areaRepository, never()).save(any(Area.class));
        }

        @Test
        @DisplayName("Should not check name uniqueness when name unchanged")
        void update_WithSameName_ShouldNotCheckUniqueness() {
            // Arrange
            UpdateAreaRequest request = new UpdateAreaRequest(
                    userId,
                    "Strategist", // same name as existing
                    "#00AAFF",
                    "MEDIUM",
                    true
            );

            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));
            when(areaRepository.save(any(Area.class))).thenReturn(area);
            when(areaMapper.toResponse(area)).thenReturn(areaResponse);

            // Act
            areaService.update(areaId, request);

            // Assert
            verify(areaRepository, never()).existsByUserIdAndNameAndIdNot(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("delete() method tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete area successfully when user is owner")
        void delete_WithValidOwner_ShouldDeleteArea() {
            // Arrange
            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));

            // Act
            areaService.delete(areaId, userId);

            // Assert
            verify(areaRepository).findById(areaId);
            verify(areaRepository).delete(area);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when area not found")
        void delete_WithNonExistingArea_ShouldThrowException() {
            // Arrange
            when(areaRepository.findById(areaId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> areaService.delete(areaId, userId))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(areaRepository).findById(areaId);
            verify(areaRepository, never()).delete(any(Area.class));
        }

        @Test
        @DisplayName("Should throw AccessDeniedException when user is not owner")
        void delete_WithDifferentUserId_ShouldThrowAccessDeniedException() {
            // Arrange
            UUID differentUserId = UUID.randomUUID();
            when(areaRepository.findById(areaId)).thenReturn(Optional.of(area));

            // Act & Assert
            assertThatThrownBy(() -> areaService.delete(areaId, differentUserId))
                    .isInstanceOf(AccessDeniedException.class);

            verify(areaRepository).findById(areaId);
            verify(areaRepository, never()).delete(any(Area.class));
        }
    }
}
