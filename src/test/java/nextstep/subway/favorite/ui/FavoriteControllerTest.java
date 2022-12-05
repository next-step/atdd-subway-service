package nextstep.subway.favorite.ui;

import static org.mockito.ArgumentMatchers.any;

import java.net.URI;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    @Mock
    private FavoriteService favoriteService;

    @Test
    void 즐겨찾기_추가_테스트() {
        //given
        Mockito.when(favoriteService.saveFavorite(any(), any()))
            .thenReturn(new FavoriteResponse(1L, null, null));
        FavoriteController favoriteController = new FavoriteController(favoriteService);

        //when
        ResponseEntity<Void> response = favoriteController
            .createFavorites(LoginMember.GUEST, new FavoriteRequest());

        //then
        Assertions.assertThat(response.getHeaders().getLocation())
            .isEqualTo(URI.create("/favorites/1"));

    }
}