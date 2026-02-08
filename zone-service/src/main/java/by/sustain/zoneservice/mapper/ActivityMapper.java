package by.sustain.zoneservice.mapper;

import by.sustain.zoneservice.dto.activity.ActivityResponse;
import by.sustain.zoneservice.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public ActivityResponse toResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getArea().getId(),
                activity.getName()
        );
    }
}
