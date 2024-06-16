package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Todo {
    @Id @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    private String title;
    private String memo;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id") //fk명
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="proposer_id") //fk명
    private Member proposer;

    @OneToMany(mappedBy = "todo")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="location_id")
    private  Location location;
}
