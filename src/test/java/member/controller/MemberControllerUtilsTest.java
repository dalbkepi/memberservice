package member.controller;

import member.MemberServiceApplication;
import member.controller.dto.GetMemberDto;
import member.controller.dto.PostMemberDto;
import member.entity.Member;
import member.repository.MemberRepository;
import member.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemberServiceApplication.class)
public class MemberControllerUtilsTest {

    @Test
    public void mapDtoToMember() {
        PostMemberDto dto = new PostMemberDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPostalCode("12345");
        Date now = new Date();
        dto.setDateBirth(now);

        Member member = MemberControllerUtils.mapDtoToMember(dto);
        assertNotNull(member);
        assertEquals("John", member.getFirstName());
        assertEquals("Doe", member.getLastName());
        assertEquals("12345", member.getPostalCode());
        assertEquals(now, member.getDateBirth());
    }

    @Test
    public void mapMembersToDtos() {
        List<Member> members = new ArrayList<>();
        Date now = new Date();
        members.add(new Member("John", "Doe", now, "12345"));

        List<GetMemberDto> dtos = MemberControllerUtils.mapMembersToDtos(members);
        assertEquals(1, dtos.size());

        GetMemberDto dto = new GetMemberDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDateBirth(now);
        dto.setPostalCode("12345");

        assertTrue(dtos.contains(dto));
    }

    @Test
    public void mapMemberToDto() {
        Date now = new Date();
        Member member = new Member("John", "Doe", now, "12345");
        member.setId(1l);

        GetMemberDto dto = MemberControllerUtils.mapMemberToDto(member);
        assertNotNull(dto);
        assertTrue(1l == member.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(now, dto.getDateBirth());
        assertEquals("12345", dto.getPostalCode());
    }
}