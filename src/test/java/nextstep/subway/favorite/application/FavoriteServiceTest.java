package nextstep.subway.favorite.application;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.member.domain.MemberTestFixture.createLoginMember;
import static nextstep.subway.member.domain.MemberTestFixture.createMember;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("즐겨찾기 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Line 칠호선;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 이수역;
    private Station 반포역;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Member 회원;
    private LoginMember 로그인한_회원;

    @BeforeEach
    public void setUp() {
        이수역 = createStation("이수역");
        반포역 = createStation("반포역");
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");

        칠호선 = createLine("칠호선", "bg-khaki", 이수역, 반포역, 20);
        신분당선 = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-green", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-orange", 교대역, 양재역, 5);
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, 3));

        회원 = createMember("email@email.com", "password", 28);
        로그인한_회원 = createLoginMember(회원);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest("1", "2");
        Favorite favorite = Favorite.of(회원, 강남역, 교대역);
        when(memberRepository.findById(any())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(교대역));

        // when
        FavoriteResponse response = favoriteService.createFavorite(로그인한_회원, favoriteRequest);

        // then
        assertAll(
                () -> assertThat(response.getSource().getName()).isEqualTo("강남역"),
                () -> assertThat(response.getTarget().getName()).isEqualTo("교대역")
        );
    }

    @DisplayName("즐겨찾기 생성 시, 존재하지 않는 지하철역을 시작점으로 하면 예외를 발생시킨다.")
    @Test
    void createFavoriteThrowErrorWhenSourceStationIsNotExists() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest("1", "2");
        when(memberRepository.findById(any())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.createFavorite(로그인한_회원, favoriteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.존재하지_않는_역.getErrorMessage());
    }

    @DisplayName("즐겨찾기 생성 시, 존재하지 않는 지하철역을 종착점으로 하면 예외를 발생시킨다.")
    @Test
    void createFavoriteThrowErrorWhenTargetStationIsNotExists() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest("1", "2");
        when(memberRepository.findById(any())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> favoriteService.createFavorite(로그인한_회원, favoriteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.존재하지_않는_역.getErrorMessage());
    }

    @DisplayName("즐겨찾기 생성 시, 이미 존재하는 즐겨찾기라면 예외를 발생시킨다.")
    @Test
    void createFavoriteThrowErrorWhenFavoriteIsDuplicated() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest("1", "2");
        when(memberRepository.findById(any())).thenReturn(Optional.of(회원));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(남부터미널역));
        favoriteService.createFavorite(로그인한_회원, favoriteRequest);

        // when & then
        assertThatThrownBy(() -> favoriteService.createFavorite(로그인한_회원, favoriteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이미_존재하는_즐겨찾기.getErrorMessage());
    }

    @DisplayName("즐겨찾기 전체 목록을 조회한다.")
    @Test
    void findFavorites() {
        // given
        회원.addFavorite(Favorite.of(회원, 강남역, 양재역));
        회원.addFavorite(Favorite.of(회원, 교대역, 이수역));
        when(memberRepository.findById(회원.getId())).thenReturn(Optional.of(회원));

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(로그인한_회원);

        // then
        assertAll(
                () -> assertThat(favorites).hasSize(2),
                () -> assertThat(favorites.stream().map(FavoriteResponse::getSource).map(StationResponse::getName))
                        .containsExactlyElementsOf(Arrays.asList("강남역", "교대역")),
                () -> assertThat(favorites.stream().map(FavoriteResponse::getTarget).map(StationResponse::getName))
                        .containsExactlyElementsOf(Arrays.asList("양재역", "이수역"))
        );
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        Favorite favorite = Favorite.of(회원, 반포역, 강남역);
        회원.addFavorite(favorite);
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));
        when(memberRepository.findById(any())).thenReturn(Optional.of(회원));

        // when
        favoriteService.deleteFavorite(로그인한_회원, 1L);

        // then
        assertThat(favorite).isNotIn(회원.favorites());
    }

    @DisplayName("삭제를 시도하는 회원의 즐겨찾기가 아닌 항목을 삭제하려하면 예외를 발생시킨다.")
    @Test
    void deleteFavoriteThrowErrorWhenNotAuthorizedMemberTryDelete() {
        // given
        Member 다른회원 = createMember("email2@email.com", "password2", 28);
        Favorite favorite = Favorite.of(다른회원, 반포역, 강남역);
        회원.addFavorite(favorite);
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));
        when(memberRepository.findById(any())).thenReturn(Optional.of(회원));

        // when & then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(로그인한_회원, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.자신의_즐겨찾기여야_함.getErrorMessage());
    }
}
