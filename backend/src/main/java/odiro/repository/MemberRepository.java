package odiro.repository;


import io.lettuce.core.dynamic.annotation.Param;
import odiro.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByusername(String username);
    Optional<Member> findByEmail(String email);
    List<Member> findByUsernameContaining(String username);
}
