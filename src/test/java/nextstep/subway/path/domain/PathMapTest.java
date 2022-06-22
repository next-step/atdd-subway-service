package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathMapTest {
    @Mock
    private LineRepository lineRepository;

    private Station 선릉역;
    private Station 정자역;
    private Station 수원역;
    private Station 남문역;
    private Line 분당선;

    @BeforeEach
    void beforeEach() {
        선릉역 = new Station(1L, "선릉역");
        정자역 = new Station(2L, "정자역");
        수원역 = new Station(3L, "수원역");
        남문역 = new Station(3L, "남문역");
        분당선 = new Line("분당선", "bg-yellow-400");
        분당선.addLineStation(new Section(분당선, 선릉역, 정자역, 40));
        분당선.addLineStation(new Section(분당선, 정자역, 수원역, 10));
    }

    @Test
    void createMap() {
        // given
        when(lineRepository.findAll()).thenReturn(Collections.singletonList(분당선));

        // then
        WeightedMultigraph<Station, DefaultWeightedEdge> map = new PathMap().createMap(lineRepository.findAll());

        // given
        assertThat(map.containsVertex(선릉역)).isTrue();
        assertThat(map.containsVertex(정자역)).isTrue();
        assertThat(map.containsVertex(수원역)).isTrue();
        assertThat(map.containsVertex(남문역)).isFalse();

        assertThat(map.containsEdge(선릉역, 정자역)).isTrue();
        assertThat(map.containsEdge(정자역, 수원역)).isTrue();
        assertThat(map.containsEdge(선릉역, 수원역)).isFalse();
    }
}
