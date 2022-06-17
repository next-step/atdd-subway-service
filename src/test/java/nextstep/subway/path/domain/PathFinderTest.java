package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;
    private Station 춘천역;
    private Station 강릉역;
    private Station 경주역;
    private Station 포항역;
    private Line 분당선;
    private Line 강릉선;
    private Line 당릉선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        춘천역 = new Station(4L, "춘천역");
        강릉역 = new Station(5L, "강릉역");
        경주역 = new Station(6L, "경주역");
        포항역 = new Station(7L, "포항역");
        분당선 = new Line("분당선", "bg-yellow-400");
        강릉선 = new Line("강릉선", "bg-blue-600");
        당릉선 = new Line("당릉선", "bg-blue-600");
        분당선.addLineStation(new Section(분당선, 선릉역, 정자역, 40));
        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 10));
        강릉선.addLineStation(new Section(강릉선, 정자역, 춘천역, 40));
        강릉선.addLineStation(new Section(강릉선, 춘천역, 강릉역, 20));
        당릉선.addLineStation(new Section(당릉선, 수원역, 춘천역, 10));
    }

    @Test
    void findPath_sameLine() {
        // when
        PathFinder pathFinder = new PathFinder(lineRepository, stationRepository);
        pathFinder.addLineStation(분당선);
        PathResponse result = pathFinder.findShortestPath(선릉역, 수원역);

        // then
        assertThat(result.getStations()).containsExactly(선릉역, 정자역, 수원역);
        assertThat(result.getDistance()).isEqualTo(50);
    }

    @Test
    void findPath_differentLines() {
        // when
        PathFinder pathFinder = new PathFinder(lineRepository, stationRepository);
        pathFinder.addLineStations(Arrays.asList(분당선, 강릉선, 당릉선));
        PathResponse result = pathFinder.findShortestPath(선릉역, 강릉역);

        // then
        assertThat(result.getStations()).containsExactly(선릉역, 정자역, 수원역, 춘천역, 강릉역);
        assertThat(result.getDistance()).isEqualTo(80);
    }

    @Test
    void findPath_throwsException_ifStationsAreSame() {
        // when
        // then
        assertThatThrownBy(() -> {
            PathFinder pathFinder = new PathFinder(lineRepository, stationRepository);
            pathFinder.addLineStations(Arrays.asList(분당선, 강릉선, 당릉선));
            pathFinder.findShortestPath(선릉역, 선릉역);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    void findPath_throwsException_ifStationsAreNotConnected() {
        // given
        Line 동해선 = new Line("동해선", "bg-white-400", 경주역, 포항역, 20);

        // when
        // then
        PathFinder pathFinder = new PathFinder(lineRepository, stationRepository);
        pathFinder.addLineStations(Arrays.asList(분당선, 강릉선, 당릉선, 동해선));
        assertThatThrownBy((() -> pathFinder.findShortestPath(선릉역, 경주역)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findPath_throwsException_ifFromOrToNonExistentStation() {
        // when
        // then
        PathFinder pathFinder = new PathFinder(lineRepository, stationRepository);
        pathFinder.addLineStations(Arrays.asList(분당선, 강릉선, 당릉선));
        assertAll(
                () -> assertThatThrownBy((() -> pathFinder.findShortestPath(선릉역, 경주역)))
                        .isInstanceOf(RuntimeException.class),
                () -> assertThatThrownBy((() -> pathFinder.findShortestPath(경주역, 선릉역)))
                        .isInstanceOf(RuntimeException.class)
        );
    }
}