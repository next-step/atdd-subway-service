package nextstep.subway.member.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.application.MemberNotFoundException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthMemberControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY2" +
            "9tIiwiaWF0IjoxNjI0OTUwMzc1LCJleHAiOjE2MjQ5NTAzNzV9.tdP5i5LV8VrQkfADPBgGFCMLYc3MkqPXZm74zGa8wQ8";
    private static final String INVALID_TOKEN = VALID_TOKEN + "_INVALID";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    private LoginMember loginMember;
    private LoginMember nullLoginMember;
    private MemberResponse memberResponse;
    private MemberRequest memberRequest;

    @BeforeEach
    void setUp() {
        nullLoginMember = new LoginMember();
        loginMember = new LoginMember(1L, "test@test.com", 10);
        memberResponse = new MemberResponse(1L, "test@test.com", 10);
        memberRequest = new MemberRequest("test2@test.com", "password2", 15);
    }

    @Test
    void findMemberOfMineWithValidToken() throws Exception {
        when(authService.findMemberByToken(eq(VALID_TOKEN)))
                .thenReturn(loginMember);
        when(memberService.findMember(anyLong()))
                .thenReturn(memberResponse);

        mockMvc.perform(
                get("/members/me")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(memberResponse)));
    }

    @Test
    void findMemberOfMineWithInvalidToken() throws Exception {
        when(authService.findMemberByToken(eq(INVALID_TOKEN)))
                .thenReturn(nullLoginMember);
        when(memberService.findMember(null))
                .thenThrow(new MemberNotFoundException());

        mockMvc.perform(
                get("/members/me")
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void findMemberOfMineWithNotExistToken() throws Exception {
        when(authService.findMemberByToken(null))
                .thenReturn(nullLoginMember);
        when(memberService.findMember(null))
                .thenThrow(new MemberNotFoundException());

        mockMvc.perform(
                get("/members/me")
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMemberOfMineWithValidToken() throws Exception {
        when(authService.findMemberByToken(eq(VALID_TOKEN)))
                .thenReturn(loginMember);

        mockMvc.perform(
                put("/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isOk());

        verify(memberService).updateMember(anyLong(), any(MemberRequest.class));
    }

    @Test
    void updateMemberOfMineWithInvalidToken() throws Exception {
        when(authService.findMemberByToken(eq(INVALID_TOKEN)))
                .thenReturn(nullLoginMember);
        doThrow(MemberNotFoundException.class)
                .when(memberService).updateMember(any(), any(MemberRequest.class));

        mockMvc.perform(
                put("/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .header("Authorization", "Bearer " + INVALID_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(memberService, never()).updateMember(anyLong(), any(MemberRequest.class));
    }

    @Test
    void updateMemberOfMineWithNotExistToken() throws Exception {
        when(authService.findMemberByToken(null))
                .thenReturn(nullLoginMember);
        doThrow(MemberNotFoundException.class)
                .when(memberService).updateMember(any(), any(MemberRequest.class));

        mockMvc.perform(
                put("/members/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest))
        )
                .andExpect(status().isNotFound());

        verify(memberService, never()).updateMember(anyLong(), any(MemberRequest.class));
    }

    @Test
    void deleteMemberOfMine() throws Exception {
        when(authService.findMemberByToken(eq(VALID_TOKEN)))
                .thenReturn(loginMember);

        mockMvc.perform(
                delete("/members/me")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(anyLong());
    }
}
