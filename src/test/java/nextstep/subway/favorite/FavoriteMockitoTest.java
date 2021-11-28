package nextstep.subway.favorite;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.ui.FavoriteController;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 - mockito를 활용한 가짜 협력 객체 사용")
public class FavoriteMockitoTest {

    private final Station 역삼역 = new Station("역삼역");
    private final Station 강남역 = new Station("강남역");
    private final Member 최웅석 = new Member("email", "1234", 10);
    private final LoginMember 로그인_최웅석 = new LoginMember(1L, "email", 10);
    private final FavoriteRequest favoriteRequest = FavoriteRequest.of(강남역.getId(), 역삼역.getId());

    @Test
    @DisplayName("지하철 노선 즐겨찾기")
    void findAllLines() {
        // given
        FavoriteService favoriteService = mock(FavoriteService.class);
        StationService stationService = mock(StationService.class);
        MemberService memberService = mock(MemberService.class);

        when(memberService.findOneMember(1L)).thenReturn(최웅석);
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(역삼역);

        when(favoriteService.saveFavorite(로그인_최웅석, favoriteRequest))
                .thenReturn(FavoriteResponse.of(new Favorite(최웅석, 강남역, 역삼역)));
        FavoriteController favoriteController = new FavoriteController(favoriteService);

        // when
        ResponseEntity<FavoriteResponse> response = favoriteController.saveFavorites(로그인_최웅석, favoriteRequest);

        // then
        assertThat(response.getBody().getSource()).isEqualTo(강남역);
        assertThat(response.getBody().getTarget()).isEqualTo(역삼역);
    }

}
