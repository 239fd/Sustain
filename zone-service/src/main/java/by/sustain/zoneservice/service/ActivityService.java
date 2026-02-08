package by.sustain.zoneservice.service;

import by.sustain.zoneservice.dto.activity.ActivityListResponse;
import by.sustain.zoneservice.dto.activity.ActivityResponse;
import by.sustain.zoneservice.dto.activity.CreateActivityRequest;
import by.sustain.zoneservice.dto.activity.UpdateActivityRequest;
import by.sustain.zoneservice.entity.Activity;
import by.sustain.zoneservice.entity.Area;
import by.sustain.zoneservice.exception.ResourceAlreadyExistsException;
import by.sustain.zoneservice.exception.ResourceNotFoundException;
import by.sustain.zoneservice.mapper.ActivityMapper;
import by.sustain.zoneservice.repository.ActivityRepository;
import by.sustain.zoneservice.repository.AreaRepository;
import by.sustain.zoneservice.service.impl.IActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService implements IActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final AreaRepository areaRepository;

    @Override
    @Transactional
    public ActivityResponse create(CreateActivityRequest request) {
        UUID areaId = request.areaId();

        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> ResourceNotFoundException.areaNotFound(areaId));

        if (activityRepository.existsByAreaIdAndName(areaId, request.name())) {
            throw ResourceAlreadyExistsException.activityAlreadyExists(request.name(), area.getName());
        }

        Activity activity = new Activity();
        activity.setName(request.name());
        activity.setArea(area);

        Activity saved = activityRepository.save(activity);
        return activityMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityListResponse getAllByAreaId(UUID areaId) {
        if (!areaRepository.existsById(areaId)) {
            throw ResourceNotFoundException.areaNotFound(areaId);
        }

        List<ActivityResponse> activities = activityRepository.findAllByAreaId(areaId).stream()
                .map(activityMapper::toResponse)
                .toList();

        return new ActivityListResponse(activities);
    }

    @Override
    @Transactional
    public ActivityResponse update(UUID activityId, UpdateActivityRequest request) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> ResourceNotFoundException.activityNotFound(activityId));

        if (!request.name().equals(activity.getName())) {
            UUID areaId = activity.getArea().getId();
            if (activityRepository.existsByAreaIdAndNameAndIdNot(areaId, request.name(), activityId)) {
                throw ResourceAlreadyExistsException.activityAlreadyExists(request.name(), activity.getArea().getName());
            }
            activity.setName(request.name());
        }

        Activity saved = activityRepository.save(activity);
        return activityMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> ResourceNotFoundException.activityNotFound(activityId));
        activityRepository.delete(activity);
    }
}
