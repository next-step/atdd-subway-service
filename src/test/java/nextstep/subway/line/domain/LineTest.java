package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
    }

    @Test
    void update() {
        Line line = Line.builder()
                .name("신분당선")
                .color("red lighten-1")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .build();

        line.update("2호선", "green lighten-2");
        assertThat(line.getName()).isEqualTo("2호선");
        assertThat(line.getColor()).isEqualTo("green lighten-2");
    }
}
