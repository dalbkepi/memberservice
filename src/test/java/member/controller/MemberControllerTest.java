package member.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import member.MemberServiceApplication;
import member.controller.dto.PostMemberDto;
import member.controller.dto.PutMemberDto;
import member.entity.Member;
import member.exception.MemberNotFoundException;
import member.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemberServiceApplication.class)
@WebAppConfiguration
public class MemberControllerTest {

    private MockMvc mockMvc;

    private MediaType contentTypeJson = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MediaType contentTypeXml = new MediaType(MediaType.APPLICATION_XML.getType(),
            MediaType.APPLICATION_XML.getSubtype());

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        memberRepository.deleteAllInBatch();

        Member member = new Member("John", "Doe", new Date(), "12345");
        memberRepository.save(member);
    }

    @Test
    public void createMember() throws Exception {
        PostMemberDto dto = new PostMemberDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        Date now = new Date();
        dto.setDateBirth(now);
        dto.setPostalCode("12345");

        mockMvc.perform(post("/members").content(toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentTypeJson))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.dateBirth", is(now.getTime())))
                .andExpect(jsonPath("$.postalCode", is("12345")));
    }

    @Test
    public void createMemberWithValidationError() throws Exception {
        PostMemberDto dto = new PostMemberDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setDateBirth(new Date());
        dto.setPostalCode("1234");

        ResultActions result = mockMvc.perform(post("/members").content(toJson(dto))
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isBadRequest());
        result.andReturn().getResponse().getErrorMessage().equals("Validation error");
        result.andReturn().getResolvedException().equals(MethodArgumentNotValidException.class);
    }

    @Test
    public void getMember() throws Exception {
        Member member = memberRepository.findByFirstNameAndLastName("John", "Doe");
        mockMvc.perform(get("/members/" + member.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentTypeJson))
                .andExpect(jsonPath("$.id", is(member.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.dateBirth", is(member.getDateBirth().getTime())))
                .andExpect(jsonPath("$.postalCode", is("12345")));
    }

    @Test
    public void getNonExistingMember() throws Exception {
        ResultActions result = mockMvc.perform(get("/members/0").contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
        result.andReturn().getResponse().getErrorMessage().equals("No such member");
        result.andReturn().getResolvedException().equals(MemberNotFoundException.class);
    }

    @Test
    public void updateMember() throws Exception {
        Member member = memberRepository.findByFirstNameAndLastName("John", "Doe");
        PutMemberDto dto = new PutMemberDto();
        dto.setFirstName("Jane");
        dto.setLastName(member.getLastName());
        dto.setDateBirth(member.getDateBirth());
        dto.setPostalCode(member.getPostalCode());

        mockMvc.perform(put("/members/" + member.getId()).content(toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentTypeJson))
                .andExpect(jsonPath("$.id", is(member.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    public void updateNonExisting() throws Exception {
        PutMemberDto dto = new PutMemberDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setDateBirth(new Date());
        dto.setPostalCode("12345");

        ResultActions result = mockMvc.perform(put("/members/0").content(toJson(dto))
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
        result.andReturn().getResponse().getErrorMessage().equals("No such member");
        result.andReturn().getResolvedException().equals(MemberNotFoundException.class);
    }

    @Test
    public void deleteMember() throws Exception {
        Member member = memberRepository.findByFirstNameAndLastName("John", "Doe");

        mockMvc.perform(delete("/members/" + member.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertNull(memberRepository.findOne(member.getId()));
    }

    @Test
    public void deleteNonExistingMember() throws Exception {

        ResultActions result = mockMvc.perform(delete("/members/0").contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
        result.andReturn().getResponse().getErrorMessage().equals("No such member");
        result.andReturn().getResolvedException().equals(MemberNotFoundException.class);
    }

    @Test
    public void getMembers() throws Exception {
        Member member = memberRepository.findByFirstNameAndLastName("John", "Doe");
        mockMvc.perform(get("/members").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentTypeJson))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].dateBirth", is(member.getDateBirth().getTime())))
                .andExpect(jsonPath("$[0].postalCode", is("12345")))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private String toJson(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Test
    public void xmlRequestAndResponse() throws Exception {
        PostMemberDto dto = new PostMemberDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        Date now = new Date();
        dto.setDateBirth(now);
        dto.setPostalCode("12345");

        String dtoString = "/GetMemberDto";
        mockMvc.perform(
                post("/members")
                        .content(toXml(dto))
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentTypeXml))
                .andExpect(xpath(dtoString + "/id").exists())
                .andExpect(xpath(dtoString + "/firstName").string("Jane"))
                .andExpect(xpath(dtoString + "/lastName").string("Doe"))
                .andExpect(xpath(dtoString + "/dateBirth").string(""+now.getTime()))
                .andExpect(xpath(dtoString + "/postalCode").string("12345"));
    }

    private String toXml(Object o) throws IOException {
        XmlMapper mapper = new XmlMapper();
        return mapper.writeValueAsString(o);
    }
}