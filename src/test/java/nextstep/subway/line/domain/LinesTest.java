package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LinesTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Line 신분당선;
    private Line 이호선;
    private Lines lines;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
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
                .distance(10)
                .addedFare(300)
                .build();
        lines = new Lines(Arrays.asList(신분당선, 이호선));
    }

    @DisplayName("컬렉션에서 노선의 존재여부")
    @Test
    void isEmpty() {
        assertThat(lines.isEmpty()).isFalse();
    }

    @DisplayName("노선의 가장 큰 추가요금을 찾는다.")
    @Test
    void getMaxAddedFare() {
        Fare maxAddedFare = lines.getMaxAddedFare();
        assertThat(maxAddedFare.value().intValue()).isEqualTo(300);
    }
}
