package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteService favoriteService;

    @DisplayName("즐겨찾기 등록 테스트 Mockito 활용")
    @Test
    void createFavorite() {
        // given
        FavoriteResponse favoriteResponse = FavoriteResponse.from(Favorite.createEmpty());
        doReturn(favoriteResponse).when(favoriteService).createFavorites(any(LoginMember.class), any(FavoriteRequest.class));

        // when
        FavoriteResponse response = favoriteService.createFavorites(new LoginMember(), FavoriteRequest.of(1L, 2L));

        // then
        assertThat(response).isNotNull();
    }
}