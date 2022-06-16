package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("경로의 노선 중 제일 큰 추가요금을 가져온다.")
    void additionalLineFare() {
        Station 신림역 = new Station("신림역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");

        Line 이호선 = new Line("2호선", "green", 신림역, 교대역, 10, 500);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 양재역, 20, 1000);

        List<SectionEdge> sectionEdges = new ArrayList<>();
        이호선.getSections().getSections()
            .forEach(section -> sectionEdges.add(new SectionEdge(section)));
        삼호선.getSections().getSections()
            .forEach(section -> sectionEdges.add(new SectionEdge(section)));

        List<Station> stations = Arrays.asList(신림역, 교대역, 양재역);

        Path path = new Path(stations, 30, sectionEdges);

        assertThat(path.additionalLineFare()).isEqualTo(1000);
    }

}
