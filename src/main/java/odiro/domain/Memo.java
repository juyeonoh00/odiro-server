package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Memo {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="day_plan_id")
    private DayPlan dayPlan;

    protected Memo () {
    }

    public Memo(DayPlan dayPlan, String content)
    {
        this.dayPlan = dayPlan;
        this.content = content;
    }

}

