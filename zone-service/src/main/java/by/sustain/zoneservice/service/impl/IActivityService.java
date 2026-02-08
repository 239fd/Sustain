package by.sustain.zoneservice.service.impl;

import by.sustain.zoneservice.dto.activity.ActivityListResponse;
import by.sustain.zoneservice.dto.activity.ActivityResponse;
import by.sustain.zoneservice.dto.activity.CreateActivityRequest;
import by.sustain.zoneservice.dto.activity.UpdateActivityRequest;

import java.util.UUID;

public interface IActivityService {

    ActivityResponse create(CreateActivityRequest request);

    ActivityListResponse getAllByAreaId(UUID areaId);

    ActivityResponse update(UUID activityId, UpdateActivityRequest request);

    void delete(UUID activityId);
}
