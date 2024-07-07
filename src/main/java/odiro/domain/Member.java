//package odiro.domain;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter @Setter
//public class Member {
//    @Id @GeneratedValue
//    @Column(name="member_id")
//    private Long id;
//    private String userId;
//    private String password;
//    private String name;
//    private String email;
//    private String profileImage;
//
//    @OneToMany(mappedBy = "initializer")
//    private List<Plan> initalizedPlans = new ArrayList<>();
//
//    @OneToMany(mappedBy = "writer")
//    private List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "participant")
//    private List<PlanMember> joinedPlan = new ArrayList<>();
//
//
//    public Member() {
//    }
//
//    public Member(String userId) {
//        this.userId = userId;
//    }
//}