package by.sustain.zoneservice.repository;

import by.sustain.zoneservice.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> findAllByAreaId(UUID areaId);


    boolean existsByAreaIdAndName(UUID areaId, String name);

    boolean existsByAreaIdAndNameAndIdNot(UUID areaId, String name, UUID id);
}
