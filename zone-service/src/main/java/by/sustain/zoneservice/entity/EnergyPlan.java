package by.sustain.zoneservice.entity;

import by.sustain.zoneservice.entity.enums.Interval;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "energy_plan", schema = "zone")
@Getter
@Setter
@NoArgsConstructor
public class EnergyPlan {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "duration")
    private Interval duration = Interval.MONTH;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "rhythm")
    private Interval rhythm;

    @Column(nullable = false, name = "frequency")
    private Integer frequency = 0;

    @Column(name = "date_start")
    private OffsetDateTime dateStart;

    @Column(name = "date_finish")
    private OffsetDateTime dateFinish;

    @OneToMany(mappedBy = "energyPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanProgress> planProgresses = new ArrayList<>();

}
