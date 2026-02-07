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
@Table(name = "activity", schema = "zone")
@Getter
@Setter
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, name = "id")
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name = "No name";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityInProgress> activitiesInProgress = new ArrayList<>();

}
