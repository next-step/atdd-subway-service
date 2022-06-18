package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineFarePolicyTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .upStation(강남역)
                .downStation(양재역)
                .distance(Distance.from(10))
                .extraFare(Fare.from(900))
                .build();
        이호선 = new Line.Builder("이호선", "bg-red-600")
                .upStation(교대역)
                .downStation(강남역)
                .distance(Distance.from(10))
                .extraFare(Fare.from(0))
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-red-600")
                .upStation(교대역)
                .downStation(양재역)
                .distance(Distance.from(5))
                .extraFare(Fare.from(500))
                .build();
    }

    @Test
    void 노선_추가_요금() {
        LineFarePolicy lineFarePolicy = new LineFarePolicy();

        assertThat(lineFarePolicy.calculate(Arrays.asList(신분당선, 이호선))).isEqualTo(Fare.from(900));
    }
}