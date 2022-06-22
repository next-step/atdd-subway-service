package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class SubwayGraphTest {
    final private Station 강남역 = new Station(1L, "강남역");
    final private Station 양재역 = new Station(2L, "양재역");
    final private Station 교대역 = new Station(3L, "교대역");
    final private Station 남부터미널역 = new Station(4L, "남부터미널역");
    final private Station 여의도역 = new Station(5L, "여의도역");
    final private Station 샛강역 = new Station(6L, "샛강역");
    final private Line 신분당선 = new Line(1L, "신분당선", "red");
    final private Line 이호선 = new Line(2L, "이호선", "green");
    final private Line 삼호선 = new Line(3L, "삼호선", "orange");
    final private Line 구호선 = new Line(4L, "구호선", "brown");
    final int 강남역_양재역_간_거리 = 20;
    final int 교대역_강남역_간_거리 = 10;
    final int 교대역_남부터미널역_간_거리 = 10;
    final int 남부터미널역_양재역_간_거리 = 5;
    final int 여의도역_샛강역_간_거리 = 15;
    final Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, 강남역_양재역_간_거리);
    final Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 교대역_강남역_간_거리);
    final Section 교대역_남부터미널역_구간 = new Section(삼호선, 교대역, 남부터미널역, 교대역_남부터미널역_간_거리);
    final Section 남부터미널역_양재역_구간 = new Section(삼호선, 남부터미널역, 양재역, 남부터미널역_양재역_간_거리);
    final Section 여의도역_샛강역_구간 = new Section(구호선, 여의도역, 샛강역, 여의도역_샛강역_간_거리);
    final List<Section> sections = Arrays.asList(
            강남역_양재역_구간,
            교대역_강남역_구간,
            교대역_남부터미널역_구간,
            남부터미널역_양재역_구간,
            여의도역_샛강역_구간);
    final List<Station> stations = Arrays.asList(
            강남역,
            양재역,
            교대역,
            남부터미널역,
            여의도역,
            샛강역);

    @Test
    void 최단_경로를_구할_수_있다() {
        // given
        final SubwayGraph subwayGraph = new SubwayGraph(sections, stations);

        // when
        final Path path = subwayGraph.findShortestPath(강남역, 남부터미널역);

        // then
        assertThat(path.getDistance()).isEqualTo(교대역_강남역_간_거리 + 교대역_남부터미널역_간_거리);
        assertThat(path.getStations()).containsExactly(강남역, 교대역, 남부터미널역);
    }

    @Test
    void 출발역과_도착역이_같으면_에러가_발생해야_한다() {
        // given
        final SubwayGraph subwayGraph = new SubwayGraph(sections, stations);

        // when and then
        assertThatThrownBy(() -> subwayGraph.findShortestPath(강남역, 강남역))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 경로가_없으면_에러가_발생해야_한다() {
        // given
        final SubwayGraph subwayGraph = new SubwayGraph(sections, stations);

        // when and then
        assertThatThrownBy(() -> subwayGraph.findShortestPath(강남역, 여의도역))
                .isInstanceOf(RuntimeException.class);
    }
}
