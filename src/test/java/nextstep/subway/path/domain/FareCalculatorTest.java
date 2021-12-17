package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public class FareCalculatorTest {

    @DisplayName("요금 계산 확인 - 거리별")
    @ParameterizedTest
    @CsvSource(value = { "1:1250", "10:1250", "11:1350", "15:1350", "16:1450", "50:2050", "51:2150", "58:2150", "59:2250", "67:2350" }, delimiter = ':')
    void 거리별_요금_계산_확인(int distance, int expected) {
        // when
        int 요금 = FareCalculator.calculator(distance);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
    @DisplayName("요금 계산 확인 - 노선별 추가요금")
    @ParameterizedTest
    @CsvSource(value = { "1:1550", "10:1550", "11:1650", "15:1650", "16:1750", "50:2350", "51:2450", "58:2450", "59:2550", "67:2650" }, delimiter = ':')
    void 노선별_추가요금_계산_확인(int distance, int expected) {
        // when
        Station 교대역 = Station.from("교대역");
        Station 강남역 = Station.from("강남역");
        Line 이호선 = Line.of("이호선", "bg-green-600", 교대역, 강남역, Distance.from(30), 300);
        int 요금 = FareCalculator.calculator(Lines.of(Arrays.asList(이호선)), Arrays.asList(교대역, 강남역), distance);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
    @DisplayName("요금 계산 확인 - 연령별 할인요금")
    @ParameterizedTest
    @CsvSource(value = { "1:7:600", "10:15:960", "11:30:1650" }, delimiter = ':')
    void 연령별_요금_계산_확인(int distance, int age, int expected) {
        // when
        Station 교대역 = Station.from("교대역");
        Station 강남역 = Station.from("강남역");
        Line 이호선 = Line.of("이호선", "bg-green-600", 교대역, 강남역, Distance.from(30), 300);
        int 요금 = FareCalculator.calculator(Lines.of(Arrays.asList(이호선)), Arrays.asList(교대역, 강남역), distance, age);

        // then
        assertThat(요금).isEqualTo(expected);
    }
}
