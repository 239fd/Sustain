package by.sustain.zoneservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "timer", schema = "zone")
@Getter
@Setter
@NoArgsConstructor
public class Timer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_in_progress_id", nullable = false)
    private ActivityInProgress activityInProgress;

    @Column(nullable = false, name = "start")
    private OffsetDateTime start;

}
