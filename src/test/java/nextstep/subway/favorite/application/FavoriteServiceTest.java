package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.EMAIL;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.PASSWORD;
import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.favorite.FavoriteAcceptanceTest.SOURCE;
import static nextstep.subway.favorite.FavoriteAcceptanceTest.TARGET;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@DisplayName("즐겨찾기 서비스 기능 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private Station 강남역;
    private Station 양재역;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberRepository memberRepository;
    private FavoriteRequest favoriteRequest;
    private LoginMember loginMember;
    private Member member;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");

        favoriteRequest = new FavoriteRequest(SOURCE, TARGET);
        loginMember = new LoginMember(1L, EMAIL, AGE);
        member = new Member(1L, EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("즐겨찾기 생성 기능 테스트")
    void createFavoriteTest() {
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);

        즐겨찾기_생성_환경_설정됨();

        FavoriteResponse favoriteResponse = favoriteService.createFavorite(favoriteRequest, loginMember);
        즐겨찾기_생성_검증됨(favoriteResponse);
    }

    private void 즐겨찾기_생성_환경_설정됨() {
        lenient().when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        lenient().when(stationRepository.findById(2L)).thenReturn(Optional.of(양재역));
        lenient().when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Favorite favorite = new Favorite(1L, 강남역, 양재역, member);
        lenient().when(favoriteRepository.save(any())).thenReturn(favorite);
    }

    private void 즐겨찾기_생성_검증됨(FavoriteResponse favoriteResponse) {
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource()).isNotNull();
        assertThat(favoriteResponse.getTarget()).isNotNull();
    }
}