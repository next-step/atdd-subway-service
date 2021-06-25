package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.ui.SameSourceTargetException;
import nextstep.subway.path.ui.SourceTargetNotConnectException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

    List<Station> allStations;
    Lines lines;

    Station 강남역;
    Station 역삼역;
    Station 선릉역;
    Station 교대역;
    Station 남부터미널역;
    Station 양재역;
    Station 매봉역;
    Station 도곡역;
    Station 한티역;
    Station 동춘역;
    Station 동막역;
    Station 공사중역;

    PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");
        매봉역 = new Station("매봉역");
        도곡역 = new Station("도곡역");
        한티역 = new Station("한티역");
        동춘역 = new Station("동춘역");
        동막역 = new Station("동막역");

        allStations = Arrays.asList(강남역, 역삼역, 선릉역, 교대역, 남부터미널역, 양재역, 매봉역, 도곡역, 한티역, 동춘역, 동막역);

        Line 이호선 = new Line("이호선", "green", 강남역, 역삼역, 10, 200);
        이호선.addSection(new Section(역삼역, 선릉역, 10));
        이호선.addSection(new Section(교대역, 강남역, 10));

        Line 삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 10, 300);
        삼호선.addSection(new Section(남부터미널역, 양재역, 10));
        삼호선.addSection(new Section(양재역, 매봉역, 10));
        삼호선.addSection(new Section(매봉역, 도곡역, 10));

        Line 분당선 = new Line("분당선", "yellow", 선릉역, 한티역, 10, 400);
        분당선.addSection(new Section(한티역, 도곡역, 10));

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10, 500);

        Line 인천선 = new Line("인천선", "blue", 동춘역, 동막역, 10, 0);

        lines = new Lines(Arrays.asList(이호선, 삼호선, 분당선, 신분당선, 인천선));

        pathFinder = new PathFinder(allStations, lines);
    }

    @DisplayName("최단 경로 찾기")
    @Test
    void 최단_경로_찾기() {
        //when
        List<Station> shortestPath = pathFinder.shortestPath(남부터미널역, 한티역);

        //then
        assertThat(shortestPath.stream().map(Station::getName)).containsExactly(남부터미널역.getName(), 양재역.getName(), 매봉역.getName(), 도곡역.getName(), 한티역.getName());
    }

    @DisplayName("최단 경로 거리")
    @Test
    void 최단_경로_거리() {
        //when
        int shortestWeight = pathFinder.shortestWeight(강남역, 한티역);

        //then
        assertThat(shortestWeight).isEqualTo(30);
    }

    @DisplayName("같은 출발역, 도착역")
    @Test
    void 같은_출발역_도착역() {
        //when, then
        assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 강남역)).isInstanceOf(SameSourceTargetException.class);
    }

    @DisplayName("출발역, 도착역 연결 안됨")
    @Test
    void 출발역_도착역_연결_안됨() {
        //when, then
        assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 동춘역)).isInstanceOf(SourceTargetNotConnectException.class);
    }

    @DisplayName("출발역이 노선에 없음")
    @Test
    void 출발역이_노선에_없음() {
        //when, then
        assertThatThrownBy(() -> pathFinder.shortestPath(공사중역, 동춘역)).isInstanceOf(SourceTargetNotConnectException.class);
    }
}
