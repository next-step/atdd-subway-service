package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
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
        Favorite favorite = Favorite.of(Station.of(1L, "강남역"), Station.of(2L, "판교역"), new Member());
        FavoriteResponse favoriteResponse = FavoriteResponse.from(favorite);
        doReturn(favoriteResponse).when(favoriteService).createFavorites(any(LoginMember.class), any(FavoriteRequest.class));

        // when
        FavoriteResponse response = favoriteService.createFavorites(new LoginMember(), FavoriteRequest.of(1L, 2L));

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("즐겨찾기 목록 조회 Mockito 활용")
    @Test
    void showFavorite() {
        // given
        Favorite favorite = Favorite.of(Station.of(1L, "강남역"), Station.of(2L, "판교역"), new Member());
        List<FavoriteResponse> favoriteResponses = Lists.newArrayList(FavoriteResponse.from(favorite));
        doReturn(favoriteResponses).when(favoriteService).showFavorites(any(LoginMember.class));

        // when
        List<FavoriteResponse> responses = favoriteService.showFavorites(new LoginMember());

        // then
        assertThat(responses).hasSize(1);
    }
}