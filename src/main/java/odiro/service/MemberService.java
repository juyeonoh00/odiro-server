package travelplaner.odiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelplaner.odiro.domain.Member;
import travelplaner.odiro.domain.Plan;
import travelplaner.odiro.mysql.MemberRepository;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long memberId) {
        Member findmember = memberRepository.findById(memberId).get();
        return findmember;
    }

    public Plan findAllPlans(Member member) {

    }
}
