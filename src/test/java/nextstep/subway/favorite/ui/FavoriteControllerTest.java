package nextstep.subway.favorite.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY2" +
            "9tIiwiaWF0IjoxNjI0OTUwMzc1LCJleHAiOjE2MjQ5NTAzNzV9.tdP5i5LV8VrQkfADPBgGFCMLYc3MkqPXZm74zGa8wQ8";
    private static final Long SOURCE = 1L;
    private static final Long TARGET = 2L;
    private static final Long givenLoginMemberId = 1L;
    private static final String givenEmail = "test@test.com";
    private static final int givenAge = 20;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private AuthService authService;

    private FavoriteRequest favoriteRequest;
    private Favorite favorite;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        favoriteRequest = new FavoriteRequest(SOURCE, TARGET);
        loginMember = new LoginMember(givenLoginMemberId, givenEmail, givenAge);
        favorite = new Favorite(1L, givenLoginMemberId, SOURCE, TARGET);

        when(authService.findMemberByToken(any()))
                .thenReturn(loginMember);
    }

    @Test
    void createFavorite() throws Exception {
        when(favoriteService.saveFavorite(anyLong(), anyLong(), anyLong()))
                .thenReturn(favorite);

        mockMvc.perform(
                post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriteRequest))
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(header().exists("Location"))
                .andExpect(status().isCreated());
    }
}
