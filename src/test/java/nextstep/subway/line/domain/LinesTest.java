package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LinesTest {

    private final int MAX_EXTRA_FARE = 10000;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재시민의숲;
    private Station 교대역;
    private Station 선릉역;
    private Station 매봉역;
    private Lines lines;

    @BeforeEach
    void setUp() {
        강남역 = 지하철역_생성("강남역");
        양재시민의숲 = 지하철역_생성("양재시민의숲");
        교대역 = 지하철역_생성("교대역");
        선릉역 = 지하철역_생성("선릉역");
        매봉역 = 지하철역_생성("매봉역");

        신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재시민의숲, 20, MAX_EXTRA_FARE);
        이호선 = 지하철_노선_생성("이호선", "bg-green-600", 교대역, 선릉역, 30, 200);
        삼호선 = 지하철_노선_생성("삼호선", "bg-orange-600", 교대역, 매봉역, 30, 300);

        lines = Lines.from(Arrays.asList(신분당선, 이호선, 삼호선));
    }


    @Test
    @DisplayName("가장 추가요금이 많은 노선의 추가요금을 반환한다")
    void maxExtraFare() {
        // when
        int maxExtraFare = lines.maxExtraFare();

        // then
        assertThat(maxExtraFare).isEqualTo(MAX_EXTRA_FARE);
    }


    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }

    private Line 지하철_노선_생성(String name, String color, Station upStation, Station downStation, int distance, int extraFare) {
        return new Line.Builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .extraFare(extraFare)
                .build();
    }
}
