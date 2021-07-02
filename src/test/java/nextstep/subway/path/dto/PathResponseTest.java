package nextstep.subway.path.dto;

import nextstep.subway.component.domain.SectionWeightedEdge;
import nextstep.subway.component.domain.SubwayGraph;
import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathResponseTest {

    private Station 강남역;
    private Station 광교역;
    private Station 교대역;
    private Line 신분당선;
    private Line 삼호선;
    private List<Line> lines;
    private SubwayGraph subwayGraph;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        교대역 = new Station(3L, "교대역");
        신분당선 = new Line(1L, "신분당선", 강남역, 광교역, 10);
        삼호선 = new Line(2L, "삼호선", 교대역, 강남역, 5);
        lines = Arrays.asList(신분당선, 삼호선);
        subwayGraph = new SubwayGraph(SectionWeightedEdge.class);
        subwayGraph.addVertexesAndEdge(lines);
    }

    @Test
    void subwayPath_객체를_이용하여_PatchResponse객체_생성() {
        List<SectionWeightedEdge> edges = new ArrayList<>(subwayGraph.getGraph().edgeSet());
        ArrayList<Station> stations = new ArrayList<>(subwayGraph.getGraph().vertexSet());
        SubwayPath subwayPath = new SubwayPath(edges, stations);
        PathResponse pathResponse = PathResponse.of(subwayPath);
        StationResponse stationResponse_강남역 = new StationResponse(강남역.getId(), 강남역.getName(), null, null);
        StationResponse stationResponse_광교역 = new StationResponse(광교역.getId(), 광교역.getName(), null, null);
        StationResponse stationResponse_교대역 = new StationResponse(교대역.getId(), 교대역.getName(), null, null);
        assertThat(pathResponse.getStations()).containsExactly(stationResponse_강남역, stationResponse_광교역, stationResponse_교대역);
        assertThat(pathResponse.getDistance()).isEqualTo(15);
    }
}