package odiro.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import odiro.domain.member.Member;

import java.time.LocalDateTime;


@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;
    private LocalDateTime writeTime;

    @PrePersist
    public void prePersist() {
        this.writeTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.writeTime = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="day_plan_id")
    private DayPlan dayPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer_id")
    private Member writer;

    protected Comment () {
    }

    public Comment(DayPlan dayPlan, Member writer, String content)
    {
        this.writer = writer;
        this.content = content;
        this.dayPlan = dayPlan;
    }

}
