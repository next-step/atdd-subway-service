package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {
    @DisplayName("경로 찾기")
    @Test
    void findPath() {
        // then
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        lines.add(이호선);
        lines.add(삼호선);

        // when
        Path path = new Path(lines, 강남역, 양재역);

        //then
        assertThat(path.findShortestPath()).containsExactly(강남역, 양재역);
        assertThat(path.findWeight()).isEqualTo(10);
    }
}
