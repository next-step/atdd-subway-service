package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.fixture.DomainLayerSubwayFixture;
import org.junit.jupiter.api.Test;

class SubwayGraphTest {
    private DomainLayerSubwayFixture 지하철 = new DomainLayerSubwayFixture();
    private final List<Section> sections = Arrays.asList(
            지하철.강남역_양재역_구간,
            지하철.교대역_강남역_구간,
            지하철.교대역_남부터미널역_구간,
            지하철.남부터미널역_양재역_구간,
            지하철.여의도역_샛강역_구간);
    private final List<Station> stations = Arrays.asList(
            지하철.강남역,
            지하철.양재역,
            지하철.교대역,
            지하철.남부터미널역,
            지하철.여의도역,
            지하철.샛강역);

    @Test
    void 최단_경로를_구할_수_있다() {
        // given
        final SubwayGraph subwayGraph = new SubwayGraph(sections, stations);

        // when
        final Path path = subwayGraph.findShortestPath(지하철.강남역, 지하철.남부터미널역);

        // then
        assertThat(path.getDistance()).isEqualTo(지하철.교대역_강남역_간_거리 + 지하철.교대역_남부터미널역_간_거리);
        assertThat(path.getStations()).containsExactly(지하철.강남역, 지하철.교대역, 지하철.남부터미널역);
    }

    @Test
    void 출발역과_도착역이_같으면_에러가_발생해야_한다() {
        // given
        final SubwayGraph subwayGraph = new SubwayGraph(sections, stations);

        // when and then
        assertThatThrownBy(() -> subwayGraph.findShortestPath(지하철.강남역, 지하철.강남역))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 경로가_없으면_에러가_발생해야_한다() {
        // given
        final SubwayGraph subwayGraph = new SubwayGraph(sections, stations);

        // when and then
        assertThatThrownBy(() -> subwayGraph.findShortestPath(지하철.강남역, 지하철.여의도역))
                .isInstanceOf(RuntimeException.class);
    }
}
