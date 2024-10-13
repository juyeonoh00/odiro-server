//package odiro.domain;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import odiro.domain.member.Member;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//public class PlanMember {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="plan_member_id")
//    private Long id;
//
//    // 플랜 멤버
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.LAZY)
//    private List<Member> planMembers = new ArrayList<>();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="plan_id")
//    private Plan plan;
//}
