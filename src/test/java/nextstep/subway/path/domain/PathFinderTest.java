package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
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
    private Station 서울역;
    private Station 용산역;
    private Station 혜화역;
    private PathFinder pathFinder;
    private LoginMember loginMember;
    private LoginMember 어린이;
    private LoginMember 청소년;

    /**
     * 방배역 -3- 서초역 -4- 교대역 -5- 강남역 -6- 역삼역 -12- 선릉역 (2호선 800원)
     *                      |         |
     *                      3         3
     *                      |         |
     *                남부터미널역 -4- 양재역 -4- 양재시민의숲 -13- 청계산입구 (신분당선 900원)
     *                 (3호선 700원)
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
        서울역 = new Station("서울역");
        ReflectionTestUtils.setField(서울역, "id", 11L);
        용산역 = new Station("용산역");
        ReflectionTestUtils.setField(용산역, "id", 12L);
        혜화역 = new Station("혜화역");
        ReflectionTestUtils.setField(혜화역, "id", 13L);

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 청계산입구역, 20, 900);
        신분당선.addSection(강남역, 양재역, 3);
        신분당선.addSection(양재역, 양재시민의숲역, 4);

        Line 이호선 = new Line("이호선", "bg-red-500", 방배역, 선릉역, 30, 800);
        이호선.addSection(방배역, 서초역, 3);
        이호선.addSection(서초역, 교대역, 4);
        이호선.addSection(교대역, 강남역, 5);
        이호선.addSection(강남역, 역삼역, 6);

        Line 삼호선 = new Line("삼호선", "bg-red-400", 교대역, 양재역, 7, 700);
        삼호선.addSection(교대역, 남부터미널역, 3);

        Line 일호선 = new Line("일호선", "bg-red-300", 서울역, 용산역, 10);

        pathFinder = new PathFinder(Lists.newArrayList(신분당선, 이호선, 삼호선, 일호선));
        loginMember = new LoginMember(1L, "email@nexstep.com", 30);
        어린이 = new LoginMember(2L, "email@nexstep.com", 7);
        청소년 = new LoginMember(3L, "email@nexstep.com", 15);
    }

    @DisplayName("출발역과 도착역이 서로 같은 노선일 경우 최단 경로를 리턴한다. (신분당선)")
    @Test
    void findSameLinePath1() {
        //when
        SubwayShortestPath actual = pathFinder.findPath(강남역, 청계산입구역, loginMember);

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
        SubwayShortestPath actual = pathFinder.findPath(방배역, 선릉역, loginMember);

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
        SubwayShortestPath actual = pathFinder.findPath(교대역, 양재역, loginMember);

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
        SubwayShortestPath actual = pathFinder.findPath(방배역, 청계산입구역, loginMember);

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
    void startStationIsSameAsEndStation() {
        //when
        assertThatThrownBy(() -> pathFinder.findPath(강남역, 강남역, loginMember))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(PathFinder.START_STATION_IS_SAME_AS_END_STATION_EXCEPTION_MESSAGE);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외를 발생시킨다.")
    @Test
    void notConnectedStation() {
        //when
        assertThatThrownBy(() -> pathFinder.findPath(서울역, 강남역, loginMember))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(PathFinder.STATION_IS_NOT_CONNECTED_EXCEPTION_MESSAGE);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외를 발생시킨다.")
    @Test
    void notExistStation() {
        //when
        assertThatThrownBy(() -> pathFinder.findPath(서울역, 혜화역, loginMember))
                .isInstanceOf(IllegalArgumentException.class) //then
                .hasMessage(PathFinder.NOT_EXIST_STATION_EXCEPTION_MESSAGE);
    }

    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
    @Test
    void transferFare() {
        //when 2호선+신분당선
        SubwayShortestPath actual = pathFinder.findPath(강남역, 청계산입구역, loginMember);

        //then 거리 20, 노선 최고금액 800
        assertThat(actual.getFare()).isEqualTo(2350);
    }

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50% 할인된 요금을 적용한다.")
    @Test
    void transferFareWithChild() {
        //when 2호선+신분당선
        SubwayShortestPath actual = pathFinder.findPath(강남역, 청계산입구역, 어린이);

        //then 거리 20, 노선 최고금액 800 -> 2350
        assertThat(actual.getFare()).isEqualTo(1000);
    }

    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20% 할인된 요금을 적용한다.")
    @Test
    void transferFareWithTeenager() {
        //when 2호선+신분당선
        SubwayShortestPath actual = pathFinder.findPath(강남역, 청계산입구역, 청소년);

        //then 거리 20, 노선 최고금액 800 -> 2350
        assertThat(actual.getFare()).isEqualTo(1600);
    }
}
