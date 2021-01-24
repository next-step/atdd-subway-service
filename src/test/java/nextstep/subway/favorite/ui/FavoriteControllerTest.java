package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Stubbing : SpringBean MockBean 사용")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class FavoriteControllerTest {
    @MockBean
    private FavoriteService favoriteService;

    @Test
    void createFavoriteWithBadRequest() {
        when(favoriteService.createFavorite(any())).thenThrow(RuntimeException.class);
        FavoriteController favoriteController = new FavoriteController(favoriteService);

        LoginMember user = new LoginMember(1L, "test@test.com", 40);
        FavoriteRequest favoriteRequest = new FavoriteRequest(user, 1L, 4L);
        //ResponseEntity responseEntity = favoriteController.createFavorite(favoriteRequest);

        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}