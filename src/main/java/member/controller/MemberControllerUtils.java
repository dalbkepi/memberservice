package member.controller;

import member.controller.dto.GetMemberDto;
import member.controller.dto.PostMemberDto;
import member.entity.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberControllerUtils {

    public static Member mapDtoToMember(PostMemberDto dto) {
        Member member = new Member();
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setDateBirth(dto.getDateBirth());
        member.setPostalCode(dto.getPostalCode());
        return member;
    }

    public static List<GetMemberDto> mapMembersToDtos(List<Member> members) {
        List<GetMemberDto> dtos = new ArrayList<>();
        for (Member member : members) {
            dtos.add(mapMemberToDto(member));
        }
        return dtos;
    }

    public static GetMemberDto mapMemberToDto(Member member) {
        GetMemberDto dto = new GetMemberDto();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setDateBirth(member.getDateBirth());
        dto.setPostalCode(member.getPostalCode());
        return dto;
    }
}