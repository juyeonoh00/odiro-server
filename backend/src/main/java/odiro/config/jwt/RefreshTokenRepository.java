package odiro.config.jwt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


// save() 메소드에 에세스 토큰 + 리프레시 토큰 + memberId 저장
// 로그아웃 시 delete() 메소드로 리프레시 토큰 제거
// 만료된 access token으로 리프레시 토큰을 찾음
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {
    Optional<RefreshToken> findByAccessToken(String accessToken);
}