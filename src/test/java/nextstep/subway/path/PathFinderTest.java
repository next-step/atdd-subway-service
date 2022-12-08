package nextstep.subway.path;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.ExtraFee;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

    final static Station 강남역 = new Station("강남역");
    final static Station 역삼역 = new Station("역삼역");
    final static Station 교대역 = new Station("교대역");
    final static Station 양재시민의숲역 = new Station("양재시민의숲역");
    final static Station 양재역 = new Station("양재역");
    final static Station 남부터미널역 = new Station("남부터미널역");
    final static Station 인천역 = new Station("인천역");
    final static Station 동인천역 = new Station("동인천역");
    final static Station 도원역 = new Station("도원역");
    final static Station 부평역 = new Station("부평역");
    final static Station 신도림역 = new Station("신도림역");
    final static Station 서울역 = new Station("서울역");
    final static Station 동두천역 = new Station("동두천역");
    final static Station 판교역 = new Station("판교역");
    final static Station 정자역 = new Station("정자역");
    final static Station 경기광주역 = new Station("경기광주역");

    final static Line 일호선 = new Line ("일호선", "bg-blue-600");
    final static Line 이호선 = new Line ("이호선", "bg-green-600");
    final static Line 삼호선 = new Line ("이호선", "bg-yellow-600");
    final static Line 신분당선 = new Line("신분당선", "bg-red-600", new ExtraFee(100));
    final static Line 경강선 = new Line("경강선", "bg-blue-400", new ExtraFee(200));

    final static Member 어린이 = new Member("test@test.com","1234", 10);
    final static Member 청소년 = new Member("test2@test.com","1234", 15);


    /**
     * 교대역    --- *2호선* ---   강남역    --- *2호선* ---   역삼역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역    --- *신분당선* ---   양재시민의숲역
     *                          |
     *                          *신분당선*
     *                          |
     *                          판교역    --- *경강선* ---   경기광주역
     *                          |
     *                          *신분당선*
     *                          |
     *                          정자역
     * 
     * 인천역    --- *1호선* ---   동인천역     --- *1호선* ---   도원역     --- *1호선* ---   신도림역     --- *1호선* ---   서울역     --- *1호선* ---   동두천역
     */
    @BeforeAll
    static void setUp() {
        일호선.addSection(new Section(일호선, 동두천역, 서울역, new Distance(40)));
        일호선.addSection(new Section(일호선, 서울역, 신도림역, new Distance(10)));
        일호선.addSection(new Section(일호선, 신도림역, 부평역, new Distance(27)));
        일호선.addSection(new Section(일호선, 부평역, 도원역, new Distance(14)));
        일호선.addSection(new Section(일호선, 도원역, 동인천역, new Distance(6)));
        일호선.addSection(new Section(일호선, 동인천역, 인천역, new Distance(5)));

        이호선.addSection(new Section(이호선, 역삼역, 강남역, new Distance(10)));
        이호선.addSection(new Section(이호선, 강남역, 교대역, new Distance(5)));

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, new Distance(5)));
        삼호선.addSection(new Section(이호선, 남부터미널역, 양재역, new Distance(5)));

        신분당선.addSection(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        신분당선.addSection(new Section(신분당선, 양재역, 양재시민의숲역, new Distance(10)));
        신분당선.addSection(new Section(신분당선, 양재시민의숲역, 판교역, new Distance(35)));
        신분당선.addSection(new Section(신분당선, 판교역, 정자역, new Distance(25)));

        경강선.addSection(new Section(경강선, 판교역, 경기광주역, new Distance(25)));
    }

    @Test
    @DisplayName("최단 거리 경로의 지하철역 순서목록과 거리를 구한다.")
    void findShortestPathStationListTest() {
        //given
        PathFinder pathFinder = new PathFinder(역삼역, 남부터미널역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder.getShortestPathStationList())
                .containsExactly(역삼역, 강남역, 교대역, 남부터미널역);
        assertThat(pathFinder.getShortestPathDistance())
                .isEqualTo(20);
    }

    @Test
    @DisplayName("시작지점과 끝지점을 거꾸로 했을 때 거리가 동일하고 역목록은 거꾸로 나오는지 테스트")
    void sourceTargetReverseEqualPathTest() {
        //given
        PathFinder pathFinder1 = new PathFinder(역삼역, 남부터미널역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        PathFinder pathFinder2 = new PathFinder(남부터미널역, 역삼역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder1.getShortestPathStationList())
                .containsExactly(역삼역, 강남역, 교대역, 남부터미널역);
        assertThat(pathFinder2.getShortestPathStationList())
                .containsExactly(남부터미널역, 교대역, 강남역, 역삼역);
        assertThat(pathFinder1.getShortestPathDistance())
                .isEqualTo(pathFinder2.getShortestPathDistance());
    }

    @Test
    @DisplayName("한 구간의 최단 거리와 경로 구하기 테스트")
    void findOneSectionShortestPathTest() {
        //given
        PathFinder pathFinder = new PathFinder(역삼역, 강남역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder.getShortestPathStationList())
                .containsExactly(역삼역, 강남역);
        assertThat(pathFinder.getShortestPathDistance())
                .isEqualTo(10);
    }

    @Test
    @DisplayName("한 노선에서의 최단 거리와 경로 구하기 테스트")
    void findOneLineShortestPathTest() {
        //given
        PathFinder pathFinder = new PathFinder(인천역, 도원역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        //when
        //then
        assertThat(pathFinder.getShortestPathStationList())
                .containsExactly(인천역, 동인천역, 도원역);
        assertThat(pathFinder.getShortestPathDistance())
                .isEqualTo(11);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void sourceTargetEqualExceptionTest() {
        assertThatThrownBy(() -> new PathFinder(인천역, 인천역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
    void disconnectExceptionTest() {
        assertThatThrownBy(() -> new PathFinder(인천역, 강남역, Arrays.asList(일호선, 이호선, 삼호선, 신분당선)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> lineExtraFeeTest() {
        return Stream.of(
                Arguments.of(양재역, 1350),
                Arguments.of(양재시민의숲역, 1550),
                Arguments.of(판교역, 2250),
                Arguments.of(정자역, 2550),
                Arguments.of(경기광주역, 2650)
        );
    }

    @ParameterizedTest
    @DisplayName("노선 추가요금 테스트")
    @MethodSource
    void lineExtraFeeTest(Station station, int expectedFee) {
        //when
        PathFinder pathFinder = new PathFinder(강남역, station, Arrays.asList(일호선, 이호선, 삼호선, 신분당선, 경강선));
        assertThat(pathFinder.getFee()).isEqualTo(expectedFee);
    }

    static Stream<Arguments> noLoginFeeTest() {
        return Stream.of(
                Arguments.of(동인천역, 1250),
                Arguments.of(도원역, 1350),
                Arguments.of(부평역, 1550),
                Arguments.of(신도림역, 2150),
                Arguments.of(서울역, 2250),
                Arguments.of(동두천역, 2750)
        );
    }

    @ParameterizedTest
    @DisplayName("비회원 요금 테스트")
    @MethodSource
    void noLoginFeeTest(Station station, int expectedFee) {
        //when
        PathFinder pathFinder = new PathFinder(인천역, station, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        assertThat(pathFinder.getFee()).isEqualTo(expectedFee);
    }

    static Stream<Arguments> kidLoginFeeTest() {
        return Stream.of(
                Arguments.of(동인천역, 450),
                Arguments.of(도원역, 500),
                Arguments.of(부평역, 600),
                Arguments.of(신도림역, 900),
                Arguments.of(서울역, 950),
                Arguments.of(동두천역, 1200)
        );
    }

    @ParameterizedTest
    @DisplayName("어린이 요금 테스트")
    @MethodSource
    void kidLoginFeeTest(Station station, int expectedFee) {
        //when
        PathFinder pathFinder = new PathFinder(인천역, station, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        assertThat(pathFinder.getFee()).isEqualTo(expectedFee);
    }

    static Stream<Arguments> teenagerLoginFeeTest() {
        return Stream.of(
                Arguments.of(동인천역, 720),
                Arguments.of(도원역, 800),
                Arguments.of(부평역, 960),
                Arguments.of(신도림역, 1440),
                Arguments.of(서울역, 1520),
                Arguments.of(동두천역, 1920)
        );
    }

    @ParameterizedTest
    @DisplayName("청소년 요금 테스트")
    @MethodSource
    void teenagerLoginFeeTest(Station station, int expectedFee) {
        //when
        PathFinder pathFinder = new PathFinder(인천역, station, Arrays.asList(일호선, 이호선, 삼호선, 신분당선));
        assertThat(pathFinder.getFee()).isEqualTo(expectedFee);
    }

}
