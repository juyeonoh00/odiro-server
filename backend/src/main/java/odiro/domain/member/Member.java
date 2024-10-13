package odiro.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import odiro.domain.Comment;
import odiro.domain.Plan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import static lombok.AccessLevel.*;

@Entity
@Builder
@Getter@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 25)
    private String email;

    @Column(length = 225)
    private String password;

    @Column(length = 20, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Column
    private String profileImage;
    @JsonIgnore
    @OneToMany(mappedBy = "initializer")
    private List<Plan> initalizedPlans = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "planMembers")
    private List<Plan> plans = new ArrayList<>();
//
//    @JsonIgnore
//    @OneToOne(mappedBy = "participant")
//    private PlanMember joinedPlan;

    //비밀번호 암호화
    public void passwordEncoding(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    @Override
    public String toString() {
        return "Member{" +
                "authority='" + authority + '\'' +
                ", createDate=" + getCreateDate() +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}