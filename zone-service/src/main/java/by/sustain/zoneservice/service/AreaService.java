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
import by.sustain.zoneservice.service.impl.IAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AreaService implements IAreaService {

    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;

    @Override
    @Transactional
    public AreaResponse create(CreateAreaRequest request) {
        UUID userId = request.userId();

        if (areaRepository.existsByUserIdAndName(userId, request.name())) {
            throw ResourceAlreadyExistsException.areaAlreadyExists(request.name());
        }

        Area area = new Area();
        area.setUserId(userId);
        area.setName(request.name());
        area.setColor(request.color());
        area.setEnergy(EnergyLevel.valueOf(request.energy()));
        area.setBlocked(Boolean.TRUE.equals(request.isBlocked()));

        Area saved = areaRepository.save(area);
        return areaMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AreaListResponse getAllByUserId(UUID userId) {
        List<AreaResponse> areas = areaRepository.findAllByUserId(userId).stream()
                .map(areaMapper::toResponse)
                .toList();

        return new AreaListResponse(areas);
    }

    @Override
    @Transactional
    public AreaResponse update(UUID areaId, UpdateAreaRequest request) {
        UUID userId = request.userId();
        Area area = findAreaByIdAndCheckAccess(areaId, userId);

        if (request.name() != null && !request.name().equals(area.getName())) {
            if (areaRepository.existsByUserIdAndNameAndIdNot(userId, request.name(), areaId)) {
                throw ResourceAlreadyExistsException.areaAlreadyExists(request.name());
            }
            area.setName(request.name());
        }

        if (request.color() != null) {
            area.setColor(request.color());
        }

        if (request.energy() != null) {
            area.setEnergy(EnergyLevel.valueOf(request.energy()));
        }

        if (request.isBlocked() != null) {
            area.setBlocked(request.isBlocked());
        }

        Area saved = areaRepository.save(area);
        return areaMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID areaId, UUID userId) {
        Area area = findAreaByIdAndCheckAccess(areaId, userId);
        areaRepository.delete(area);
    }

    private Area findAreaByIdAndCheckAccess(UUID areaId, UUID userId) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> ResourceNotFoundException.areaNotFound(areaId));

        if (!area.getUserId().equals(userId)) {
            throw AccessDeniedException.areaAccessDenied(areaId);
        }

        return area;
    }
}
