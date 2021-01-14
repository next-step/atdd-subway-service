package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.NoSuchStationException;
import nextstep.subway.path.application.NotConnectedExeption;
import nextstep.subway.path.application.SameStartAndEndException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    private Line red = new Line("분당선", "red");
    private Line orange = new Line("3호선", "orange");

    @DisplayName("최단거리 찾기")
    @Test
    void fineShortestPath() {
        Station 강남 = new Station("강남");
        Station 광교 = new Station("광교");
        Station 교대 = new Station("교대");
        Station 양재 = new Station("양재");
        Set<Station> stations = new HashSet<>(Arrays.asList(강남, 광교, 교대, 양재));
        Section 강남교대구간 = new Section(orange, 강남, 교대, 5);
        Section 강남양재구간 = new Section(red, 강남, 양재, 5);
        Section 교대양재구간 = new Section(red, 교대, 양재, 1);
        Section 양재광교구간 = new Section(red, 양재, 광교, 10);
        List<Section> sections = Arrays.asList(강남교대구간, 강남양재구간, 교대양재구간, 양재광교구간);

        PathFinder pathFinder = new PathFinder(stations, sections);
        PathResponse shortestPath = pathFinder.findShortestPath(강남, 광교);

        assertThat(shortestPath.getDistance()).isEqualTo(15);
        assertThat(shortestPath.getStations())
                .containsExactly(StationResponse.of(강남),StationResponse.of(양재),StationResponse.of(광교));
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void sameStartEnd() {
        Station 강남 = new Station("강남");
        Station 광교 = new Station("광교");
        Set<Station> stations = new HashSet<>(Arrays.asList(강남, 광교));
        Section 강남광교구간 = new Section(orange, 강남, 광교, 50);
        List<Section> sections = Arrays.asList(강남광교구간);

        PathFinder pathFinder = new PathFinder(stations, sections);
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남, 강남))
                .isInstanceOf(SameStartAndEndException.class)
                .hasMessage("출발역과 도착역이 같습니다. 역: 강남");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void notConntected() {
        Station 강남 = new Station("강남");
        Station 광교 = new Station("광교");
        Station 교대 = new Station("교대");
        Station 양재 = new Station("양재");
        Set<Station> stations = new HashSet<>(Arrays.asList(강남, 광교, 교대, 양재));
        Section 강남광교구간 = new Section(orange, 강남, 광교, 5);
        Section 양재교대구간 = new Section(red, 양재, 교대, 5);
        List<Section> sections = Arrays.asList(강남광교구간, 양재교대구간);

        PathFinder pathFinder = new PathFinder(stations, sections);
        assertThatThrownBy(() -> pathFinder.findShortestPath(양재, 강남))
                .isInstanceOf(NotConnectedExeption.class)
                .hasMessage("연결되어 있지 않는 역입니다. 출발역: 양재 도착역: 강남");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void noExistsStation() {
        Station 강남 = new Station("강남");
        Station 광교 = new Station("광교");
        Set<Station> stations = new HashSet<>(Arrays.asList(강남, 광교));
        Section 강남광교구간 = new Section(orange, 강남, 광교, 50);
        List<Section> sections = Arrays.asList(강남광교구간);

        PathFinder pathFinder = new PathFinder(stations, sections);
        assertThatThrownBy(() -> pathFinder.findShortestPath(null, 강남))
                .isInstanceOf(NoSuchStationException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}