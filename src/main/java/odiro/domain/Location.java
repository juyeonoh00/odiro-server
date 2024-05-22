package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Location {

    @Id @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private String name;
    private String address;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="todo_id") //fk명
    private Todo todo;
}
