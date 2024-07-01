package odiro.config.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//수정 필요
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // URL 기반 CORS 설정 소스 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // CORS 설정 객체 생성
        CorsConfiguration config = new CorsConfiguration();

        // 자격 증명(쿠키, Authorization 헤더 등) 허용
        config.setAllowCredentials(true);

        // 모든 도메인 허용 (모든 출처에서의 요청을 허용)
        config.addAllowedOrigin("*");

        // 모든 헤더 허용
        config.addAllowedHeader("*");

        // 모든 HTTP 메소드 허용 (GET, POST, PUT, DELETE 등)
        config.addAllowedMethod("*");

        // 특정 URL 패턴에 대해 위에서 정의한 CORS 설정 적용
        source.registerCorsConfiguration("/api/**", config);

        // CORS 필터 생성 및 반환
        return new CorsFilter(source);
    }

}