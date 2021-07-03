package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.CustomException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.exception.CustomExceptionMessage.NOT_CONNECTED_SOURCE_AND_TARGET_STATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 혼자역;
    private Station 독립역;
    private Station 유령역;

    private List<Line> lines;

    /**
     * 교대역    --- *2호선*(10) --- 강남역
     * |                        |
     * *3호선*(3)                *신분당선*(10)
     * |                        |
     * 남부터미널역--- *3호선*(2) --- 양재
     * 3호선 300원 추가요금
     * 신분당선 1000원 추가요금
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        혼자역 = new Station("혼자역");
        독립역 = new Station("독립역");
        유령역 = new Station("나는 유령이다");

        Line 이호선 = new Line.Builder("이호선").color("RED").additionalFare(0).upStation(교대역).downStation(강남역).distance(10).build();

        Line 삼호선 = new Line.Builder("삼호선").color("GREEN").additionalFare(300).upStation(남부터미널역).downStation(양재역).distance(2).build();
        삼호선.addSection(교대역, 남부터미널역, 3);

        Line 신분당선 = new Line.Builder("신분당선").color("BLUE").additionalFare(1000).upStation(강남역).downStation(양재역).distance(10).build();

        Line 나홀로선 = new Line.Builder("나홀로선").color("GRAY").additionalFare(0).upStation(혼자역).downStation(독립역).distance(10).build();

        this.lines = Arrays.asList(이호선, 삼호선, 신분당선, 나홀로선);
    }


    @DisplayName("지하철 노선, 최단 경로 찾기 테스트")
    @Test
    void getShortestPathsTest() {
        // when
        Paths paths = PathFinder.of(lines).getShortestPaths(남부터미널역, 강남역);

        // then
        assertThat(paths.getShortestStationRoutes())
            .isNotEmpty()
            .containsExactly(남부터미널역, 양재역, 강남역);
        assertThat(paths.getTotalDistance()).isEqualTo(12);
    }

    @DisplayName("지하철 노선, 최단 경로를 찾아 요금 계산 테스트")
    @Test
    void calculateFareTest() {
        // when
        Paths paths = PathFinder.of(lines).getShortestPaths(남부터미널역, 강남역);

        // then
        assertThat(paths.getTotalDistance()).isEqualTo(12);
        assertThat(paths.calculateFare(new LoginMember())).isEqualTo(2350); // 1250 + 100 + 1000
        assertThat(paths.calculateFare(new LoginMember(1l, "joojimin@naver.com", 14))).isEqualTo(1600); // ((1250+100+1000)-350) * (1 - 0.2)
        assertThat(paths.calculateFare(new LoginMember(1l, "joojimin@naver.com", 7))).isEqualTo(1000); // ((1250+100+1000)-350) * (1 - 0.5)
    }

    @DisplayName("지하철 노선의 추가 요금 계산 테스트")
    @Test
    void calculateFareTestWithAdditionalFare() {
        // when
        Paths paths = PathFinder.of(lines).getShortestPaths(남부터미널역, 교대역);

        // then
        assertThat(paths.getTotalDistance()).isEqualTo(3);
        assertThat(paths.calculateFare(new LoginMember())).isEqualTo(1550); // 1250 + 300
    }

    @DisplayName("지하철 노선의 추가 요금 없는 기본 요금 계산 테스트")
    @Test
    void calculateFareTestWithoutAdditionalFare() {
        // when
        Paths paths = PathFinder.of(lines).getShortestPaths(교대역, 강남역);

        // then
        assertThat(paths.getTotalDistance()).isEqualTo(10);
        assertThat(paths.calculateFare(new LoginMember())).isEqualTo(1250); // 1250
    }

    @DisplayName("연결 되어있지 않은 두 역의 경로를 조회")
    @Test
    void checkConnectedBetweenStationsTest() {
        // when
        assertThatThrownBy(() -> PathFinder.of(lines).getShortestPaths(남부터미널역, 혼자역))
            .isInstanceOf(CustomException.class)
            .hasMessageContaining(NOT_CONNECTED_SOURCE_AND_TARGET_STATION.getMessage());
    }

    @DisplayName("출발역이 등록되지 않은 경우")
    @Test
    void checkNotExistedSourceStation() {
        assertThatThrownBy(() -> PathFinder.of(lines).getShortestPaths(유령역, 남부터미널역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("graph must contain the source vertex");
    }

    @DisplayName("도착역이 등록되지 않은 경우")
    @Test
    void checkNotExistedTargetStation() {
        assertThatThrownBy(() -> PathFinder.of(lines).getShortestPaths(남부터미널역, 유령역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("graph must contain the sink vertex");
    }
}
