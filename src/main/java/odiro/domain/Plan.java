package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
public class Plan {

    @Id @GeneratedValue
    @Column(name = "plan_id")
    private Long id;

    private String title;
    private Date firstDay;
    private Date lastDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="initializer_id")
    private Member initializer;

    @OneToMany(mappedBy = "plan")
    private List<Todo> todos = new ArrayList<>();

    protected Plan () {
    }

    public Plan(String title, Date firstDay, Date lastDay)
    {
        this.title = title;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

}
