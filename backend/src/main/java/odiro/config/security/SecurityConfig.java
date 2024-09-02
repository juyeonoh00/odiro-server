package odiro.config.security;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import odiro.config.jwt.JwtFilter;
import odiro.config.jwt.exception.JwtAccessDeniedException;
import odiro.config.jwt.exception.JwtAuthenticationEntryPoint;
import odiro.repository.member.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final MemberRepository memberRepository;

    private final CorsConfig corsConfig;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtFilter jwtFilter;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 해당 API에 대해서는 모든 요청을 허가
<<<<<<< Updated upstream:src/main/java/odiro/config/security/SecurityConfig.java
//                        .requestMatchers( "/api/signin").hasRole("USER")
//                                                .requestMatchers("/api/signup").hasRole("USER")
//                         이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
//                        .requestMatchers("/kakaos/**").permitAll()
//                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/kakao/**").permitAll()

=======
                        .requestMatchers("api/kakao/**").permitAll()
                        .requestMatchers("/api/emails/**", "ap/user/check-username").permitAll()
>>>>>>> Stashed changes:backend/src/main/java/odiro/config/security/SecurityConfig.java
                        .requestMatchers("/api/signin").permitAll()
                        .requestMatchers("/api/signup","/user/check-username").permitAll()
                        .requestMatchers("/","/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                        .anyRequest().authenticated())

                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex->{
                    ex.accessDeniedHandler(new JwtAccessDeniedException());
                    ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                    ex.accessDeniedHandler(new JwtAccessDeniedException());})
                .build();

    }

}





