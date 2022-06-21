package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static nextstep.subway.member.MemberAcceptanceTest.*;

@DisplayName("즐겨찾기 서비스 레이어 테스트")
@ExtendWith(SpringExtension.class)
class FavoriteServiceTest {
    @MockBean
    private FavoriteRepository favoriteRepository;

    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
    }

    @DisplayName("즐겨찾기 생성 기능 테스트")
    @Test
    void createFavorite() {
        // given
        FavoriteService favoriteService = new FavoriteService(favoriteRepository);
        LoginMember loginMember = new LoginMember(0L, EMAIL, AGE);
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, favoriteRequest);

        // then
        assertAll(
                () -> assertThat(favoriteResponse.getSource()).isEqualTo(StationResponse.of(강남역)),
                () -> assertThat(favoriteResponse.getTarget()).isEqualTo(StationResponse.of(양재역))
        );
    }

    @DisplayName("즐겨찾기 목록 조회 기능 테스트")
    @Test
    void findFavorites() {
        // given
        FavoriteService favoriteService = new FavoriteService(favoriteRepository);
        LoginMember loginMember = new LoginMember(0L, EMAIL, AGE);

        Member member = new Member(EMAIL, PASSWORD, AGE);
        Favorite favorite = Favorite.of(member, 강남역, 양재역);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(loginMember);

        // then
        assertAll(
                () -> assertThat(favoriteResponses).hasSize(1),
                () -> assertThat(favoriteResponses.get(0).getSource()).isEqualTo(StationResponse.of(강남역)),
                () -> assertThat(favoriteResponses.get(0).getTarget()).isEqualTo(StationResponse.of(양재역))
        );
    }
}