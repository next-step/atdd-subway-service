package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {
    private Station 방배역;
    private Station 서초역;
    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 양재시민의숲역;
    private Station 청계산입구역;
    private PathFinder pathFinder;

    /**
     * 방배역 -3- 서초역 -4- 교대역 -5- 강남역 -6- 역삼역 -12- 선릉역
     *                      |         |
     *                      3         3
     *                      |         |
     *                남부터미널역 -4- 양재역 -4- 양재시민의숲 -13- 청계산입구
     */
    @BeforeEach
    public void setUp() {
        방배역 = new Station("방배역");
        ReflectionTestUtils.setField(방배역, "id", 1L);
        서초역 = new Station("서초역");
        ReflectionTestUtils.setField(서초역, "id", 2L);
        교대역 = new Station("교대역");
        ReflectionTestUtils.setField(교대역, "id", 3L);
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 4L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 5L);
        선릉역 = new Station("선릉역");
        ReflectionTestUtils.setField(선릉역, "id", 6L);
        남부터미널역 = new Station("남부터미널역");
        ReflectionTestUtils.setField(남부터미널역, "id", 7L);
        양재역 = new Station("양재역");
        ReflectionTestUtils.setField(양재역, "id", 8L);
        양재시민의숲역 = new Station("양재시민의숲역");
        ReflectionTestUtils.setField(양재시민의숲역, "id", 9L);
        청계산입구역 = new Station("청계산입구역");
        ReflectionTestUtils.setField(청계산입구역, "id", 10L);

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 청계산입구역, 20);
        신분당선.addSection(강남역, 양재역, 3);
        신분당선.addSection(양재역, 양재시민의숲역, 4);

        Line 이호선 = new Line("이호선", "bg-red-500", 방배역, 선릉역, 30);
        이호선.addSection(방배역, 서초역, 3);
        이호선.addSection(서초역, 교대역, 4);
        이호선.addSection(교대역, 강남역, 5);
        이호선.addSection(강남역, 역삼역, 6);

        Line 삼호선 = new Line("삼호선", "bg-red-400", 교대역, 양재역, 7);
        삼호선.addSection(교대역, 남부터미널역, 3);

        pathFinder = new PathFinder(Lists.newArrayList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("출발역과 도착역이 서로 같은 노선일 경우 최단 경로를 리턴한다. (신분당선)")
    @Test
    void findSameLinePath1() {
        //when
        SubwayShortestPath actual = pathFinder.findPath(강남역, 청계산입구역);

        //then
        assertAll(() -> {
            assertThat(actual.getDistance()).isEqualTo(20);
            assertThat(actual.getStations()).containsExactly(
                    강남역,
                    양재역,
                    양재시민의숲역,
                    청계산입구역
            );
        });
    }

    @DisplayName("출발역과 도착역이 서로 같은 노선일 경우 최단 경로를 리턴한다. (2호선)")
    @Test
    void findSameLinePath2() {
        //when
        SubwayShortestPath actual = pathFinder.findPath(방배역, 선릉역);

        //then
        assertAll(() -> {
            assertThat(actual.getDistance()).isEqualTo(30);
            assertThat(actual.getStations()).containsExactly(
                    방배역,
                    서초역,
                    교대역,
                    강남역,
                    역삼역,
                    선릉역
            );
        });
    }

    @DisplayName("출발역과 도착역이 서로 같은 노선일 경우 최단 경로를 리턴한다. (3호선)")
    @Test
    void findSameLinePath3() {
        //when
        SubwayShortestPath actual = pathFinder.findPath(교대역, 양재역);

        //then
        assertAll(() -> {
            assertThat(actual.getDistance()).isEqualTo(7);
            assertThat(actual.getStations()).containsExactly(
                    교대역,
                    남부터미널역,
                    양재역
            );
        });
    }

    @DisplayName("출발역과 도착역이 서로 다른 노선일 경우 최단 경로를 리턴한다. (2호선+3호선+신분당선)")
    @Test
    void findOtherPath() {
        //when
        SubwayShortestPath actual = pathFinder.findPath(방배역, 청계산입구역);

        //then
        assertAll(() -> {
            assertThat(actual.getDistance()).isEqualTo(31);
            assertThat(actual.getStations()).containsExactly(
                    방배역,
                    서초역,
                    교대역,
                    남부터미널역,
                    양재역,
                    양재시민의숲역,
                    청계산입구역
            );
        });
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외를 발생시킨다.")
    @Test
    void StartStationIsSameAsEndStation() {
        //when
        assertThatThrownBy(() -> pathFinder.findPath(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PathFinder.START_STATION_IS_SAME_AS_END_STATION_EXCEPTION_MESSAGE);
    }
}
