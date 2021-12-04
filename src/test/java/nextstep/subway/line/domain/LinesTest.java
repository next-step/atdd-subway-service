package nextstep.subway.line.domain;

import nextstep.subway.common.exception.line.LineDuplicateException;
import nextstep.subway.common.exception.path.EdgeCreateException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : LinesTest
 * author : haedoang
 * date : 2021/12/05
 * description : Lines 일급 컬렉션 테스트
 */
public class LinesTest {
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
    @DisplayName("생성하기 ")
    public void create() throws Exception {
        //given
        List<Line> lineList = Arrays.asList(
                Line.of("1호선", "빨강"),
                Line.of("2호선", "주황")
        );

        //when
        Lines lines = Lines.of(lineList);

        //then
        assertThat(lines.getList()).hasSize(2);
    }

    @Test
    @DisplayName("유효성 검증 테스트")
    public void duplicateStations() throws Exception {
        // when
        List<Line> lineList = Arrays.asList(
                Line.of("1호선", "빨강"),
                Line.of("1호선", "빨강")
        );

        assertThatThrownBy(() -> Lines.of(lineList))
                .isInstanceOf(LineDuplicateException.class)
                .hasMessageContaining(LineDuplicateException.message);
    }

    @Test
    @DisplayName("Lines 객체로 간선을 등록한다.")
    public void addEdges() throws Exception {
        //given
        Stations stations = Stations.of(Arrays.asList(mockStation, mockStation2));
        Line line1 = Line.of("1호선", "빨강", mockStation, mockStation2, 5);
        Line line2 = Line.of("2호선", "노랑", mockStation, mockStation2, 5);
        Lines lines = Lines.of(Arrays.asList(line1, line2));
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.setVertex(graph);

        //when
        lines.setEdge(graph);

        //then
        assertThat(graph.vertexSet()).hasSize(2);
        assertThat(graph.edgeSet()).hasSize(2);
    }

    @Test
    @DisplayName("정점이 없을 때 간선 등록 예외처리")
    public void addEdgeException() throws Exception {
        //given
        Line line1 = Line.of("1호선", "빨강", mockStation, mockStation2, 5);
        Line line2 = Line.of("2호선", "노랑", mockStation, mockStation2, 5);
        Lines lines = Lines.of(Arrays.asList(line1, line2));
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        //when
        assertThatThrownBy(() -> lines.setEdge(graph))
                .isInstanceOf(EdgeCreateException.class)
                .hasMessageContaining(EdgeCreateException.message);
    }
}
