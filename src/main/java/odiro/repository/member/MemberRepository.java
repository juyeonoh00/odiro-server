package odiro.repository.member;

import odiro.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


//optional로 수정 후 사용 위치에서 예외 처리를 해주어야함
// ((CustomOAuth2User) authentication.getPrincipal()).getName().toString())
//                    .orElseThrow(NotFoundMemberException::new);
//                .map(this::createUserDetails)
//                        .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByusername(String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByRefreshToken(String refreshToken);
}