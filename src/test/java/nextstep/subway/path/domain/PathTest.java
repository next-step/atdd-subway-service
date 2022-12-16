package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Line 신분당선;
    private Line 이호선;
    private Lines lines;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        신분당선 = Line.builder()
                .name("신분당선")
                .color("red lighten-1")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .addedFare(100)
                .build();
        이호선 = Line.builder()
                .name("이호선")
                .color("green lighten-1")
                .upStation(교대역)
                .downStation(강남역)
                .distance(12)
                .addedFare(300)
                .build();
        lines = new Lines(Arrays.asList(신분당선, 이호선));
    }

    @DisplayName("경로를 생성한다.")
    @Test
    void 경로를_생성한다() {
        Path path = Path.of(Distance.from(22), Arrays.asList(교대역, 강남역, 양재역));

        assertAll(() -> {
            assertThat(path.getDistance()).isEqualTo(Distance.from(22));
            assertThat(path.getStations()).hasSize(3);
        });
    }
}
