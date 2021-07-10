package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 역삼역;
    private Station 남부터미널역;
    private Station 삼성역;
    private Station 계양역;
    private Station 잠실역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private PathFinder pathFinder;
    private Station 건대입구역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");
        잠실역 = new Station("잠실역");
        건대입구역 = new Station("건대입구역");
        남부터미널역 = new Station("남부터미널역");
        계양역 = new Station("계양역");
        신분당선 = new Line("신분당선", "red", 양재역, 강남역, 7, 900);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 3, 500);
        삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 5, 200);

        이호선.addSection(new Section(이호선, 강남역, 역삼역, 6));
        이호선.addSection(new Section(이호선, 역삼역, 삼성역, 10));
        이호선.addSection(new Section(이호선, 삼성역, 잠실역, 20));
        이호선.addSection(new Section(이호선, 잠실역, 건대입구역, 20));
        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다")
    void sameSourceTargetTest() {
        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외가 발생한다")
    void notConnectedSourceTargetTest() {
        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 계양역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최단경로를 조회한다")
    void findPathTest() {
        // when
        List<Station> paths = pathFinder.findPaths(남부터미널역, 역삼역);

        // then
        assertThat(paths).hasSize(4);
    }

    @Test
    @DisplayName("최단거리를 조회한다")
    void findPathsDistanceTest() {
        // when
        int pathsDistance = pathFinder.getPathsDistance(남부터미널역, 역삼역);

        // then
        assertThat(pathsDistance).isEqualTo(14);
    }

    @ParameterizedTest
    @CsvSource(value = {"10,900", "15,1440", "20,2150"})
    @DisplayName("연령 별 요금을 계산한다")
    void calculateFareByAgeTest(int age, long expected) {
        // given
        int pathsDistance = pathFinder.getPathsDistance(남부터미널역, 역삼역);

        // when
        long fare = pathFinder.getFare(남부터미널역, 역삼역, age, pathsDistance);

        // then
        assertThat(fare).isEqualTo(expected);
    }

    @Test
    @DisplayName("10KM 이내 요금을 계산한다")
    void calculateFareByDistanceTest1() {
        // given
        int age = 20;
        int pathsDistance = pathFinder.getPathsDistance(교대역, 역삼역);

        // when
        long fare = pathFinder.getFare(교대역, 역삼역, age, pathsDistance);

        // then 
        assertThat(fare).isEqualTo(1750);
    }

    @Test
    @DisplayName("50KM 이내 요금을 계산한다")
    void calculateFareByDistanceTest2() {
        // given
        int age = 20;
        int pathsDistance = pathFinder.getPathsDistance(교대역, 삼성역);

        // when
        long fare = pathFinder.getFare(교대역, 삼성역, age, pathsDistance);

        // then
        assertThat(fare).isEqualTo(2250);
    }

    @Test
    @DisplayName("50KM 초과 요금을 계산한다")
    void calculateFareByDistanceTest3() {
        // given
        int age = 20;
        int pathsDistance = pathFinder.getPathsDistance(교대역, 건대입구역);

        // when
        long fare = pathFinder.getFare(교대역, 건대입구역, age, pathsDistance);

        // then
        assertThat(fare).isEqualTo(2650);
    }
}