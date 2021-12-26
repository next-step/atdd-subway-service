package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.fixture.LineFixture;
import nextstep.subway.path.domain.fixture.SubwayGraphFixture;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.fixture.StationFixture;
import org.assertj.core.util.Lists;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayGraphTest {
    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        LineFixture.신분당선.getSections().clear();
        LineFixture.삼호선.getSections().clear();
    }

    @DisplayName("지하철 노선도 생성 테스트")
    @Test
    void 지하철_노선도_생성() {
        // when
        SubwayGraph subwayGraph = SubwayGraphFixture.지하철_노선도;

        // then
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = subwayGraph.getGraph();
        Set<Station> stations = graph.vertexSet();

        assertThat(stations).containsAll(Lists.newArrayList(
                StationFixture.동천역,
                StationFixture.광교역,
                StationFixture.강남역,
                StationFixture.양재역,
                StationFixture.양재시민의숲역,
                StationFixture.청계산입구역,
                StationFixture.미금역,
                StationFixture.정자역,
                StationFixture.판교역,
                StationFixture.교대역,
                StationFixture.남부터미널역,
                StationFixture.매봉역,
                StationFixture.도곡역,
                StationFixture.대치역
        ));
    }
}
