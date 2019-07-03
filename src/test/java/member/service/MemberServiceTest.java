package member.service;

import member.MemberServiceApplication;
import member.entity.Member;
import member.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemberServiceApplication.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void saveMember() {
        Member member = generateDefaultMember();
        Member savedMember = memberService.save(member);

        assertEquals(member, savedMember);
    }

    @Test
    public void deleteMember() {
        Member member = generateDefaultMember();
        memberRepository.save(member);

        memberService.delete(member);

        assertNull(memberRepository.findOne(member.getId()));
    }

    @Test
    public void findOneMember() {
        Member member = generateDefaultMember();
        memberRepository.save(member);

        Member foundMember = memberService.findOne(member.getId());
        assertNotNull(foundMember);
        assertEquals(member, foundMember);
    }

    @Test
    public void findAllMember() {
        Member member = generateDefaultMember();
        memberRepository.save(member);

        List<Member> members = memberService.findAll();
        assertEquals(1, members.size());
        assertTrue(members.contains(member));
    }

    private Member generateDefaultMember() {
        return new Member("John", "Doe", new Date(), "12345");
    }
}