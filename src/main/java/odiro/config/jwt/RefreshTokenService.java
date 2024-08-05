package odiro.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

//    @Transactional
//    public void saveTokenInfo(Long memberId, String refreshToken, String accessToken) {
//        refreshTokenRepository.save(new RefreshToken(String.valueOf(memberId), refreshToken, accessToken));
//    }

    // 로그아웃 시 리프레시 토큰 제거
    @Transactional
    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }
}
