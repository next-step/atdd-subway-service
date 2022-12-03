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

    @BeforeEach
    void setUp() {
        교대역 = 지하철역_생성("교대역");
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        stations = Arrays.asList(교대역, 강남역, 양재역);
    }

    @Test
    @DisplayName("지하철역 리스트와 거리, 추가요금을 받아 생성")
    void create() {
        // given
        Path path = Path.of(stations, 10, 1000);

        // expect
        assertThat(path).isNotNull();
    }

    @Test
    @DisplayName("20 거리의 추가요금 1000원 지하철 요금 계산")
    void calculateFare() {
        // given
        Path path = Path.of(stations, 20, 1000);

        // when
        int fare = path.calculateFare(0);

        // then
        assertThat(fare).isEqualTo(1_450 + 1000);
    }

    @Test
    @DisplayName("기본 거리의 추가요금 100원 청소년 지하철 요금 계산")
    void calculateFare_청소년() {
        // given
        Path path = Path.of(stations, 5, 100);

        // when
        int fare = path.calculateFare(14);

        // then
        assertThat(fare).isEqualTo(1150);
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
