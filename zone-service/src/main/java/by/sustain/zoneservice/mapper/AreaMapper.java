package by.sustain.zoneservice.mapper;

import by.sustain.zoneservice.dto.area.AreaResponse;
import by.sustain.zoneservice.entity.Area;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {

    public AreaResponse toResponse(Area area) {
        return new AreaResponse(
                area.getId(),
                area.getUserId(),
                area.getName(),
                area.getColor(),
                area.getEnergy().name(),
                area.isBlocked()
        );
    }
}
