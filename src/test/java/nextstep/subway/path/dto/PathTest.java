package nextstep.subway.path.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;

    private List<Station> stations;

    private int distance;

    @BeforeEach
    void setUp() {
        교대역 = 지하철역_생성("교대역");
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        distance = 20;
        stations = Arrays.asList(교대역, 강남역, 양재역);
    }

    @Test
    @DisplayName("지하철역 리스트와 거리를 받아 생성")
    void create() {
        // given
        Path path = Path.of(stations, distance);

        // expect
        assertThat(path).isNotNull();
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    private void 지하철_노선에_지하철역_등록(Line line, Station upStation, Station downStation, int distance) {
        line.addSection(new Section.Builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build());
    }
}
