package nextstep.subway.component.domain;

import nextstep.subway.exception.SubwayPatchException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubwayGraphTest {
    private Station 강남역;
    private Station 광교역;
    private Station 교대역;

    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        교대역 = new Station(3L, "교대역");
        신분당선 = new Line(1L, "신분당선", 강남역, 광교역, 10);
        삼호선 = new Line(2L, "삼호선", 교대역, 강남역, 5);
    }

    @Test
    void 지하철_경로_계산_도메인_객체_생성() {
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        assertThat(subwayGraph).isNotNull();
    }

    @Test
    void lines를_이용하여_지하철_경로_계산_도메인_객체에_vertex값_넣기() {
        List<Line> lines = Arrays.asList(신분당선, 삼호선);
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);

        assertThat(subwayGraph.getGraph().vertexSet()).contains(강남역, 광교역, 교대역);
    }

    @Test
    void lines를_이용하여_지하철_경로_계산_도메인_객체에_edge_값_넣기() {
        List<Line> lines = Arrays.asList(신분당선, 삼호선);
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);
        assertThat(subwayGraph.getGraph().edgeSet().size()).isEqualTo(2);
    }

    @Test
    void subwayGraph_객첵를_이용하여_최단_거리_경로_조회() {
        List<Line> lines = Arrays.asList(신분당선, 삼호선);
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);

        SubwayPath subwayPath = subwayGraph.calcShortestPath(광교역, 교대역);
        assertThat(subwayPath.getStations()).containsExactly(광교역, 강남역, 교대역);
    }

    @Test
    void subwayGraph_객체의_출발역과_도차역이_같은경우_에러_발생() {
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);

        assertThatThrownBy(() -> subwayGraph.checkValidSameStation(광교역, 광교역))
                .isInstanceOf(SubwayPatchException.class)
                .hasMessage("출발역과 도착역이 일치하면 경로를 찾을 수 없습니다.");

    }

    @Test
    void subwayGraph_객첵를_이용하여_최단_거리_경로_조회_시_연결되어있지않는_경로_조회_요청_시_에러_발생() {
        Station 사당역 = new Station(100L, "사당역");
        Station 과천역 = new Station(99L, "과천역");
        Line 사호선 = new Line(9L, "사호선", 사당역, 과천역, 10);
        List<Line> lines = Arrays.asList(신분당선, 삼호선, 사호선);
        SubwayGraph subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);

        assertThatThrownBy(() -> subwayGraph.calcShortestPath(사당역, 교대역))
                .isInstanceOf(SubwayPatchException.class)
                .hasMessage("경로를 찾을 수 없습니다. 출발역과 도착역이 노선으로 연결되어 있는지 확인해주세요.");
    }
}
