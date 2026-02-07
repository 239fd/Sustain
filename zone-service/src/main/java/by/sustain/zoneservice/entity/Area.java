package by.sustain.zoneservice.entity;

import by.sustain.zoneservice.entity.enums.EnergyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "area", schema = "zone")
@Getter
@Setter
@NoArgsConstructor
public class Area {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, name = "id")
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name = "No name";

    @Column(nullable = false, name = "color")
    private String color = "#FFFFFF";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "energy")
    private EnergyLevel energy = EnergyLevel.MEDIUM;

    @Column(nullable = false, name = "user_id")
    private UUID userId;

    @Column(nullable = false, name = "is_blocked")
    private boolean isBlocked = false;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnergyPlan> energyPlans = new ArrayList<>();

}
