package nextstep.subway.path.ui;

import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.service.PathService;
import nextstep.subway.station.application.StationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PathControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY2" +
            "9tIiwiaWF0IjoxNjI0OTUwMzc1LCJleHAiOjE2MjQ5NTAzNzV9.tdP5i5LV8VrQkfADPBgGFCMLYc3MkqPXZm74zGa8wQ8";
    private static final Long NOT_EXIST_STATION_ID1 = -1L;
    private static final Long NOT_EXIST_STATION_ID2 = -2L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @MockBean
    private AuthService authService;

    private LoginMember loginMember = new LoginMember(1L, "test@test.com", 10);

    @BeforeEach
    void setUp() {
        when(authService.findMemberByToken(eq(VALID_TOKEN)))
                .thenReturn(loginMember);
    }

    @Nested
    @DisplayName("Get /paths?source={source}&target={target}는")
    class Describe_get_path {

        @Nested
        @DisplayName("시작역과 목적역이 주어지면")
        class Context_with_valid_section {
            final Long givenSource = 1L;
            final Long givenTarget = 2L;

            @DisplayName("200 OK를 응답한다.")
            @Test
            void It_responds_ok() throws Exception {
                mockMvc.perform(

                        get("/paths?source={source}&target={target}", givenSource, givenTarget)
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 역이 주어지면")
        class Context_with_invalid_stations {
            final Long givenSource = NOT_EXIST_STATION_ID1;
            final Long givenTarget = NOT_EXIST_STATION_ID2;

            @BeforeEach
            void setUp() {
                when(pathService.findPaths(any(), anyLong(), anyLong()))
                        .thenThrow(StationNotFoundException.class);
            }

            @DisplayName("404 Not Found를 응답한다.")
            @Test
            void It_responds_not_found() throws Exception {
                mockMvc.perform(
                        get("/paths?source={source}&target={target}", givenSource, givenTarget)
                                .header("Authorization", "Bearer " + VALID_TOKEN)
                )
                        .andExpect(status().isNotFound());
            }
        }
    }
}
