package by.sustain.zoneservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "activity_in_progress", schema = "zone")
@Getter
@Setter
@NoArgsConstructor
public class ActivityInProgress {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_progress_id", nullable = false)
    private PlanProgress planProgress;

    @OneToMany(mappedBy = "activityInProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Timer> timers = new ArrayList<>();

}
