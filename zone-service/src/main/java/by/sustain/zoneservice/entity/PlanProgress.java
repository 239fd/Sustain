package by.sustain.zoneservice.entity;

import by.sustain.zoneservice.entity.enums.Interval;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "plan_progress", schema = "zone")
@Getter
@Setter
@NoArgsConstructor
public class PlanProgress {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "energy_plan_id", nullable = false)
    private EnergyPlan energyPlan;

    @Column(nullable = false, name = "took")
    private Integer took = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "timespan")
    private Interval timespan = Interval.WEEK;

    @Column(nullable = false, name = "is_done")
    private boolean isDone = false;

    @OneToMany(mappedBy = "planProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityInProgress> activitiesInProgress = new ArrayList<>();

}
