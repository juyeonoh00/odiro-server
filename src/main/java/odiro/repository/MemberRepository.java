package odiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import odiro.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
