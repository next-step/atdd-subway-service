package nextstep.subway.favorite.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.member.domain.MemberTestFixture.createLoginMember;
import static nextstep.subway.member.domain.MemberTestFixture.createMember;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoritesTest {

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

    @DisplayName("즐겨찾기들 중 찾고자 하는 즐겨찾기가 있으면 참을 반환한다.")
    @Test
    void returnTrueWhenFavoritesContainFavorite() {
        // given
        Favorites favorites = Favorites.from(Arrays.asList(Favorite.of(회원, 이수역, 반포역), Favorite.of(회원, 양재역, 반포역),
                Favorite.of(회원, 남부터미널역, 교대역)));

        // when
        boolean isContainFavorite = favorites.isContainFavorite(Favorite.of(회원, 이수역, 반포역));

        // then
        assertThat(isContainFavorite).isTrue();
    }

    @DisplayName("즐겨찾기 추가 시 이미 존재하는 즐겨찾기이면 예외를 반환한다.")
    @Test
    void addFavoriteThrowErrorWhenTryToAddDuplicateFavorite() {
        // given
        Favorites favorites = Favorites.from(Arrays.asList(Favorite.of(회원, 이수역, 반포역), Favorite.of(회원, 양재역, 반포역),
                Favorite.of(회원, 남부터미널역, 교대역)));

        // when & then
        assertThatThrownBy(() -> favorites.addFavorite(Favorite.of(회원, 양재역, 반포역)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.이미_존재하는_즐겨찾기.getErrorMessage());
    }

    @DisplayName("즐겨찾기 추가 시, 다른 회원의 즐겨찾기이면 예외를 반환한다.")
    @Test
    void addFavoriteThrowErrorWhenFavoriteOwnerIsNotMember() {
        // given
        Member 다른회원 = createMember("email2@email.com", "password2", 28);
        Favorites favorites = Favorites.from(Arrays.asList(Favorite.of(회원, 이수역, 반포역), Favorite.of(회원, 양재역, 반포역),
                Favorite.of(회원, 남부터미널역, 교대역)));

        // when & then
        assertThatThrownBy(() -> favorites.addFavorite(Favorite.of(다른회원, 강남역, 반포역)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.즐겨찾기들의_회원은_동일해야_함.getErrorMessage());
    }

    @DisplayName("즐겨찾기들 중 해당하는 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        Favorites favorites = Favorites.from(Arrays.asList(Favorite.of(회원, 이수역, 반포역), Favorite.of(회원, 양재역, 반포역),
                Favorite.of(회원, 남부터미널역, 교대역)));

        // when
        favorites.deleteFavorite(Favorite.of(회원, 양재역, 반포역));

        // then
        assertThat(Favorite.of(회원, 양재역, 반포역)).isNotIn(favorites);
    }
}
