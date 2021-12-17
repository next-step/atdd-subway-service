package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LinesTest {
    
    @Test
    @DisplayName("노선들중 최고 추가 금액 확인")
    void 최고_추가_금액_확인() {
        // given
        Station 교대역 = Station.from("교대역");
        Station 강남역 = Station.from("강남역");
        Station 양재역 = Station.from("양재역");
        Line 이호선 = Line.of("이호선", "초록색", 교대역, 강남역, Distance.from(15), 300);
        Line 신분당선 = Line.of("이호선", "빨간색", 강남역, 양재역, Distance.from(30), 900);
        Lines lines = Lines.of(Arrays.asList(이호선, 신분당선));
        
        // when, then
        assertThat(lines.calculatorMaxSurcharge(Arrays.asList(교대역, 강남역, 양재역))).isEqualTo(900);
    }

}
