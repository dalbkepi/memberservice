package member.controller.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GetMemberDto {

    private Long id;

    private String firstName;

    private String lastName;

    private Date dateBirth;

    private String postalCode;
}