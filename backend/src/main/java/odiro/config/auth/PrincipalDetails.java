package odiro.config.auth;

import jakarta.persistence.Entity;
import odiro.domain.member.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


//토큰의 유저 데이터 및 권한 관리
public class PrincipalDetails implements UserDetails {

    private final Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        System.out.println("getAuthorities : 진입");
//        GrantedAuthority authority = new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                System.out.println("getAuthority : 반환");
//                return member.getAuthority().name(); // 열거형의 이름을 반환
//            }
//        };
//        System.out.println("getAuthorities : 반환");
//        return Collections.singletonList(authority); // 단일 권한을 리스트로 반환

        System.out.println("getAuthorities : 진입");
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getAuthority().name());
        System.out.println("getAuthorities : 반환");
        return Collections.singletonList(authority);
    }

}
