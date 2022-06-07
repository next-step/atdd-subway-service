package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteServiceTest {

    @DisplayName("즐겨찾기 등록 테스트 Mockito 활용")
    @Test
    void createFavorite() {
        // given
        FavoriteService favoriteService = mock(FavoriteService.class);
        FavoriteRequest favoriteRequest = FavoriteRequest.of(1L, 2L);

        when(favoriteService.createFavorites(favoriteRequest))
                .thenReturn(FavoriteResponse.from(new Favorite(1L)));

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorites(favoriteRequest);

        // then
        assertThat(favoriteResponse).isNotNull();
    }
}