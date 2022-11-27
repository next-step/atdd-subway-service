package nextstep.subway.favorite.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.member.domain.MemberTestFixture.createLoginMember;
import static nextstep.subway.member.domain.MemberTestFixture.createMember;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 도메인 테스트")
class FavoriteTest {

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

    @DisplayName("등록하고자 하는 즐겨찾기의 출발역과 도착역이 동일하면 예외가 발생한다.")
    @Test
    void createFavoriteThrowErrorWhenSourceStationEqualsTargetStation() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(회원, 교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.출발역과_도착역이_서로_같음.getErrorMessage());
    }
}
