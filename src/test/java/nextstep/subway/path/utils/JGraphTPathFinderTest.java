package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JGraphTPathFinderTest {

    @Test
    void findPath() {
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10);
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);
        Line 삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 5);
        삼호선.addSection(new Section(교대역, 남부터미널역, 3));

        List<Line> lines = new ArrayList();
        lines.add(신분당선);
        lines.add(이호선);
        lines.add(삼호선);

        JGraphTPathFinder jGraphTPathFinder = new JGraphTPathFinder();
        PathResponse pathResponse = jGraphTPathFinder.findPath(lines, 강남역, 남부터미널역);
        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getStations().stream().map(station -> station.getName()).collect(Collectors.toList())).containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "남부터미널역"));
    }
}