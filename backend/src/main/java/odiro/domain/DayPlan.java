package odiro.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Entity
@Getter @Setter
public class DayPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_plan_id")
    private Long id;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id") //fk명
    private Plan plan;
    @JsonIgnore
    @OneToMany(mappedBy = "dayPlan")
    private List<Comment> comments = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "dayPlan")
    private List<Memo> memos = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "dayPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "location_order")
    private List<Location> locations = new ArrayList<>();

    //location_order는 자동으로 증가하며 값 저장되지 않음. 기본적으로 다 0으로 저장되는데 이러면 조회할때 마지막 한 location만 나옴.
    public void addLocation(Location location) {
        location.setLocationOrder(this.locations.size()); // 현재 리스트의 크기를 사용하여 순서 설정
        this.locations.add(location);
        location.setDayPlan(this); // 연관관계 설정
    }
}