package nextstep.subway.station.domain;

import nextstep.subway.station.exception.StationDuplicateException;
import nextstep.subway.station.exception.StationNotFoundException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * packageName : nextstep.subway.station.domain
 * fileName : StationsTest
 * author : haedoang
 * date : 2021/12/04
 * description : Station 일급 컬렉션 테스트
 */
public class StationsTest {
    Station mockStation = mock(Station.class);
    Station mockStation2 = mock(Station.class);

    @BeforeEach
    void setUp() {
        when(mockStation.getId()).thenReturn(1L);
        when(mockStation.getName()).thenReturn("강남역");
        when(mockStation2.getId()).thenReturn(2L);
        when(mockStation2.getName()).thenReturn("역삼역");
    }

    @Test
    @DisplayName("Stations을 생성한다.")
    public void create() throws Exception {
        //given
        Stations stations = Stations.of(Arrays.asList(new Station("강남역"), new Station("역삼역")));

        //then
        assertThat(stations).isNotNull();
    }

    @Test
    @DisplayName("유효성 검증 테스트")
    public void duplicateStations() throws Exception {
        // then
        assertThatThrownBy(() -> Stations.of(Arrays.asList(new Station("강남역"), new Station("강남역"))))
                .isInstanceOf(StationDuplicateException.class)
                .hasMessageContaining(StationDuplicateException.message);
    }

    @Test
    @DisplayName("역을 조회한다.")
    public void findStation() throws Exception {
        // given
        Stations stations = Stations.of(Arrays.asList(mockStation, mockStation2));

        // when
        Station station = stations.getStation(1L);

        // then
        assertThat(station).isSameAs(mockStation);
    }

    @Test
    @DisplayName("존재하지 않은 역을 조회한다.")
    public void notExistStation() throws Exception {
        // given
        Long notExistId = Long.MAX_VALUE;
        Stations stations = Stations.of(Arrays.asList(mockStation, mockStation2));

        // then
        assertThatThrownBy(() -> stations.getStation(notExistId))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining(StationNotFoundException.message);
    }

    @Test
    @DisplayName("Stations 객체로 정점을 등록한다.")
    public void addVertex() throws Exception {
        //given
        Stations stations = Stations.of(Arrays.asList(mockStation, mockStation2));
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        //when
        stations.setVertex(graph);

        //then
        assertThat(graph.vertexSet()).hasSize(2);
    }
}
