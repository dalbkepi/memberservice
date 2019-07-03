package member.controller;

import member.controller.dto.GetMemberDto;
import member.controller.dto.PostMemberDto;
import member.controller.dto.PutMemberDto;
import member.entity.Member;
import member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static member.controller.MemberControllerUtils.*;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping(
            value = "/members",
            method = RequestMethod.POST,
            consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<GetMemberDto> createMember(@RequestBody @Valid PostMemberDto dto) {
        Member member = memberService.save(mapDtoToMember(dto));
        return new ResponseEntity(mapMemberToDto(member), HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/members/{memberId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity deleteMember(@PathVariable(value = "memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        memberService.delete(member);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(
            value = "/members/{memberId}",
            method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<GetMemberDto> updateMember(@PathVariable(value = "memberId") Long memberId,
                                                     @RequestBody @Valid PutMemberDto dto) throws ParseException {

        Member member = memberService.findOne(memberId);
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setDateBirth(dto.getDateBirth());
        member.setPostalCode(dto.getPostalCode());
        member = memberService.save(member);
        return new ResponseEntity(mapMemberToDto(member), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/members/{memberId}",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<GetMemberDto> findOneMember(@PathVariable(value = "memberId") Long memberId) {
        Member member = memberService.findOne(memberId);
        return new ResponseEntity(mapMemberToDto(member), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/members",
            method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<List<GetMemberDto>> findAllMember() {
        return new ResponseEntity(mapMembersToDtos(memberService.findAll()), HttpStatus.OK);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleMethodArgumentNotValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(DefaultErrorAttributes.class.getName() + ".ERROR", null);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Validation error");
    }
}