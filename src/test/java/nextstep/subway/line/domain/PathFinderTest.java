package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Path;
import nextstep.subway.station.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PathFinderTest {

    private Line 신분당선 = new Line("신분당선", "red");
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 청계산역 = new Station("청계산역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    private Line LINE3 = new Line("3호선", "orange");
    private Station 남부터미널역 = new Station("남부터미널역");
    private Station 교대역 = new Station("교대역");

    private Line LINE2 = new Line("2호선", "green");
    private Station 서초역 = new Station("서초역");
    private Station 역삼역 = new Station("역삼역");

    @DisplayName("최단경로 조회 - 단일 노선")
    @Test
    void findPaths() {
        
        // given
        신분당선.add(강남역, 양재역, 5);
        신분당선.add(양재역, 청계산역, 10);
        신분당선.add(청계산역, 판교역, 15);
        신분당선.add(판교역, 정자역, 7);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        
        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(양재역, 정자역);
        PathResponse paths = PathResponse.of(path, new LoginMember());


        // then
        assertThat(paths.getStations().size()).isEqualTo(4);
        assertThat(paths.getDistance()).isEqualTo(32);
        List<String> stations = asNameList(paths);
        assertThat(stations).isEqualTo(Arrays.asList("양재역", "청계산역", "판교역", "정자역"));
    }

    @DisplayName("최단경로 조회 - 두 개의 노선")
    @Test
    void findPaths_twoLines() {

        // given
        신분당선.add(강남역, 양재역, 5);
        신분당선.add(양재역, 청계산역, 10);
        신분당선.add(청계산역, 판교역, 15);
        신분당선.add(판교역, 정자역, 7);

        LINE3.add(양재역, 남부터미널역, 2);
        LINE3.add(남부터미널역, 교대역, 1);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        lines.add(LINE3);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(청계산역, 교대역);
        PathResponse paths = PathResponse.of(path, new LoginMember());

        // then
        assertThat(paths.getStations().size()).isEqualTo(4);
        assertThat(paths.getDistance()).isEqualTo(13);
        List<String> stations = asNameList(paths);
        assertThat(stations).isEqualTo(Arrays.asList("청계산역", "양재역", "남부터미널역", "교대역"));
    }

    @DisplayName("최단경로 조회 - 세 개의 노선, 경로가 두 가지인 경우")
    @Test
    void findPaths_threeLinesAndTwoWay() {
        // given
        신분당선.add(강남역, 양재역, 5);
        신분당선.add(양재역, 청계산역, 10);
        신분당선.add(청계산역, 판교역, 15);
        신분당선.add(판교역, 정자역, 7);

        LINE3.add(양재역, 남부터미널역, 2);
        LINE3.add(남부터미널역, 교대역, 1);

        LINE2.add(서초역, 교대역, 1);
        LINE2.add(교대역, 강남역, 3);
        LINE2.add(강남역, 역삼역, 3);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        lines.add(LINE3);
        lines.add(LINE2);

        // when
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPaths(청계산역, 서초역);
        PathResponse paths = PathResponse.of(path, new LoginMember());

        // then
        assertThat(paths.getStations().size()).isEqualTo(5);
        assertThat(paths.getDistance()).isEqualTo(14);
        List<String> stations = asNameList(paths);
        assertThat(stations).isEqualTo(Arrays.asList("청계산역", "양재역", "남부터미널역", "교대역", "서초역"));
    }

    @DisplayName("출발지와 도착지가 동일한 경우")
    @Test
    void sourceAndTargetAreSame() {
        // given
        신분당선.add(강남역, 양재역, 5);
        신분당선.add(양재역, 청계산역, 10);
        신분당선.add(청계산역, 판교역, 15);
        신분당선.add(판교역, 정자역, 7);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);

        // when then
        PathFinder pathFinder = new PathFinder(lines);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathFinder.findPaths(양재역, 양재역))
                .withMessageMatching("출발지와 도착지가 동일합니다. 입력값을 확인해주세요.");
    }

    @DisplayName("출발지와 도착지가 연결되어 있지 않은 경우")
    @Test
    void sourceAndTargetAreNotConnected() {
        // given
        신분당선.add(양재역, 청계산역, 10);
        신분당선.add(청계산역, 판교역, 15);
        신분당선.add(판교역, 정자역, 7);

        LINE2.add(서초역, 교대역, 1);
        LINE2.add(교대역, 역삼역, 3);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        lines.add(LINE2);

        // when then
        PathFinder pathFinder = new PathFinder(lines);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathFinder.findPaths(양재역, 역삼역))
                .withMessageMatching("두 역이 서로 연결되어 있지 않습니다. 경로를 조회할 수 없습니다.");
    }

    private List<String> asNameList(PathResponse paths) {
        return paths.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }
}