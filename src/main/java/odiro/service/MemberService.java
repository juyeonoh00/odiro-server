//package odiro.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import odiro.domain.Member;
//import odiro.domain.Plan;
//import odiro.repository.MemberRepository;
//
//import java.util.Optional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class MemberService {
//
//    private final MemberRepository memberRepository;
//
//    public Long join(Member member) {
//        memberRepository.save(member);
//        return member.getId();
//    }
//
//    public Optional<Member> findById(Long memberId) {
//        return memberRepository.findById(memberId);
//    }
//
//}
