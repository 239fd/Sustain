package by.sustain.zoneservice.service.impl;

import by.sustain.zoneservice.dto.area.AreaListResponse;
import by.sustain.zoneservice.dto.area.AreaResponse;
import by.sustain.zoneservice.dto.area.CreateAreaRequest;
import by.sustain.zoneservice.dto.area.UpdateAreaRequest;

import java.util.UUID;

public interface IAreaService {

    AreaResponse create(CreateAreaRequest request);

    AreaListResponse getAllByUserId(UUID userId);

    AreaResponse update(UUID areaId, UpdateAreaRequest request);

    void delete(UUID areaId, UUID userId);
}
