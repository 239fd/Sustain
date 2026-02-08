package by.sustain.zoneservice.repository;

import by.sustain.zoneservice.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {

    List<Area> findAllByUserId(UUID userId);


    boolean existsByUserIdAndName(UUID userId, String name);

    boolean existsByUserIdAndNameAndIdNot(UUID userId, String name, UUID id);
}
