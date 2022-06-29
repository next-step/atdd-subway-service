package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LinePath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    private Line 동해선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        춘천역 = new Station(4L, "춘천역");
        강릉역 = new Station(5L, "강릉역");
        경주역 = new Station(6L, "경주역");
        포항역 = new Station(7L, "포항역");

        분당선 = new Line("분당선", "bg-yellow-400", 선릉역, 정자역, 40);
        강릉선 = new Line("강릉선", "bg-blue-600", 정자역, 춘천역, 40);
        당릉선 = new Line("당릉선", "bg-blue-600", 수원역, 춘천역, 10);
        동해선 = new Line("동해선", "bg-white-400", 경주역, 포항역, 20);

        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 10));
        강릉선.addLineStation(new Section(강릉선, 춘천역, 강릉역, 20));
    }

    @Test
    void findPath_sameLine() {
        // given
        when(lineRepository.findAll()).thenReturn(Collections.singletonList(분당선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(수원역));

        // when
        Station source = stationRepository.findById(1L).get();
        Station target = stationRepository.findById(3L).get();
        WeightedMultigraph<Station, DefaultWeightedEdge> map = new PathMap().createMap(lineRepository.findAll());
        LinePath linePath = new PathFinder().findShortestPath(map, source, target);

        // then
        assertThat(linePath.getVertex()).containsExactly(선릉역, 정자역, 수원역);
        assertThat(linePath.getWeight()).isEqualTo(50);
    }

    @Test
    void findPath_differentLines() {
        // given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(분당선, 강릉선, 당릉선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(5L)).thenReturn(Optional.of(강릉역));

        // when
        Station source = stationRepository.findById(1L).get();
        Station target = stationRepository.findById(5L).get();
        WeightedMultigraph<Station, DefaultWeightedEdge> map = new PathMap().createMap(lineRepository.findAll());
        LinePath linePath = new PathFinder().findShortestPath(map, source, target);

        // then
        assertThat(linePath.getVertex()).containsExactly(선릉역, 정자역, 수원역, 춘천역, 강릉역);
        assertThat(linePath.getWeight()).isEqualTo(80);
    }

    @Test
    void findPath_throwsException_ifStationsAreSame() {
        // given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(분당선, 강릉선, 당릉선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));

        // when
        // then
        Station source = stationRepository.findById(1L).get();
        Station target = stationRepository.findById(1L).get();
        WeightedMultigraph<Station, DefaultWeightedEdge> map = new PathMap().createMap(lineRepository.findAll());

        assertThatIllegalArgumentException().isThrownBy(() -> new PathFinder().findShortestPath(map, source, target));
    }

    @Test
    void findPath_throwsException_ifStationsAreNotConnected() {
        // given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(분당선, 강릉선, 당릉선, 동해선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(6L)).thenReturn(Optional.of(경주역));

        // when
        // then
        Station source = stationRepository.findById(1L).get();
        Station target = stationRepository.findById(6L).get();
        WeightedMultigraph<Station, DefaultWeightedEdge> map = new PathMap().createMap(lineRepository.findAll());

        assertThatIllegalArgumentException().isThrownBy(() -> new PathFinder().findShortestPath(map, source, target));
    }

    @Test
    void findPath_throwsException_ifFromOrToNonExistentStation() {
        // given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(분당선, 강릉선, 당릉선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(6L)).thenReturn(Optional.of(경주역));

        // when
        // then
        Station source = stationRepository.findById(1L).get();
        Station target = stationRepository.findById(6L).get();
        WeightedMultigraph<Station, DefaultWeightedEdge> map = new PathMap().createMap(lineRepository.findAll());

        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new PathFinder().findShortestPath(map, source, target)),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new PathFinder().findShortestPath(map, target, source))
        );
    }
}
