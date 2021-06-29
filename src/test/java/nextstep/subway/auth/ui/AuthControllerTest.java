package nextstep.subway.auth.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY2" +
            "9tIiwiaWF0IjoxNjI0OTUwMzc1LCJleHAiOjE2MjQ5NTAzNzV9.tdP5i5LV8VrQkfADPBgGFCMLYc3MkqPXZm74zGa8wQ8";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private TokenRequest tokenRequest;

    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        tokenRequest = new TokenRequest("test@test.com", "password");
        tokenResponse = new TokenResponse(TOKEN);
    }

    @Test
    void login() throws Exception {
        when(authService.login(any(TokenRequest.class)))
                .thenReturn(tokenResponse);

        mockMvc.perform(
                post("/login/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest))
        )
                .andExpect(status().isOk());
    }
}
