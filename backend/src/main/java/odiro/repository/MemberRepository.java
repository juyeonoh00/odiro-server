package odiro.repository;


import odiro.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByusername(String username);
    Optional<Member> findByEmail(String email);
}
