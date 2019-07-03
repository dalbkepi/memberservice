package member.service;

import lombok.extern.slf4j.Slf4j;
import member.entity.Member;
import member.exception.MemberNotFoundException;
import member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member save(Member member) {
        log.debug("save member {}", member);
        return memberRepository.save(member);
    }

    public void delete(Member member) {
        log.debug("delete member {}", member);
        memberRepository.delete(member);
    }

    public Member findOne(Long id) {
        log.debug("find member by id {}", id);
        Member member = memberRepository.findOne(id);
        if (member == null) {
            log.error("unable to find member by id {}", id);
            throw new MemberNotFoundException(id);
        }
        return member;
    }

    public List<Member> findAll() {
        log.debug("find all members");
        return memberRepository.findAll();
    }
}