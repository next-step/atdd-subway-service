package nextstep.subway.favorite.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AuthService;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY2" +
            "9tIiwiaWF0IjoxNjI0OTUwMzc1LCJleHAiOjE2MjQ5NTAzNzV9.tdP5i5LV8VrQkfADPBgGFCMLYc3MkqPXZm74zGa8wQ8";
    private static final Long SOURCE = 1L;
    private static final Long TARGET = 2L;
    private static final Long GIVEN_LOGIN_MEMBER_ID = 1L;
    private static final Long GIVEN_FAVORITE_ID = 1L;
    private static final String GIVEN_EMAIL = "test@test.com";
    private static final int GIVEN_AGE = 20;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private AuthService authService;

    private FavoriteRequest favoriteRequest;
    private Favorite favorite;
    private LoginMember loginMember;
    private FavoriteResponse favoriteResponse;

    @BeforeEach
    void setUp() {
        favoriteRequest = new FavoriteRequest(SOURCE, TARGET);
        loginMember = new LoginMember(GIVEN_LOGIN_MEMBER_ID, GIVEN_EMAIL, GIVEN_AGE);
        favorite = new Favorite(1L, GIVEN_LOGIN_MEMBER_ID, SOURCE, TARGET);
        favoriteResponse = new FavoriteResponse(
                favorite, StationResponse.of(new Station("강남역")), StationResponse.of(new Station("정자역"))
        );
        when(authService.findMemberByToken(any()))
                .thenReturn(loginMember);

        mockMvc = webAppContextSetup(wac).addFilter(((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })).build();
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

    @Test
    void getFavorite() throws Exception {
        when(favoriteService.getFavorites(anyLong()))
                .thenReturn(Collections.singletonList(favoriteResponse));

        mockMvc.perform(
                get("/favorites")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(Collections.singletonList(favoriteResponse)))
                );
    }

    @Test
    void deleteFavorite() throws Exception {
        mockMvc.perform(
                delete("/favorites/{id}", GIVEN_FAVORITE_ID)
                        .header("Authorization", "Bearer " + VALID_TOKEN)
        )
                .andExpect(status().isNoContent());

        verify(favoriteService).deleteFavorite(anyLong(), anyLong());
    }
}
