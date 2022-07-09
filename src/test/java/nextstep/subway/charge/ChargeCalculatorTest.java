package nextstep.subway.charge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.charge.domain.Charge;
import nextstep.subway.charge.domain.ChargeCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChargeCalculatorTest {

    private List<Section> 구간목록;

    @BeforeEach
    void setUp() {
        Line 일호선 = new Line("일호선", "blue", 0);
        Line 이호선 = new Line("일호선", "red", 500);

        Station 첫번째역 = new Station("강남역");
        Station 두번째역 = new Station("신사역");
        Station 세번째역 = new Station("부산역");

        Section 첫번째구간 = new Section(일호선, 첫번째역, 두번째역, 10);
        Section 두번째구간 = new Section(이호선, 두번째역, 세번째역, 14);

        구간목록 = new ArrayList<>();
        구간목록.add(첫번째구간);
        구간목록.add(두번째구간);
    }

    @DisplayName("최대 노선 추가요금은 500원이고 거리는 84KM에 어린이 요금 테스트")
    @Test
    void calculateKid() {
        //give
        ChargeCalculator chargeCalculator = new ChargeCalculator(10, 84, 구간목록);
        
        //when
        Charge charge = chargeCalculator.calculate();
        
        //then
        assertThat(charge.getChargeValue()).isEqualTo(1350);
    }

    @DisplayName("최대 노선 추가요금은 500원이고 거리는 10KM에 청소년 요금 테스트")
    @Test
    void calculateYouth() {
        //give
        ChargeCalculator chargeCalculator = new ChargeCalculator(15, 10, 구간목록);

        //when
        Charge charge = chargeCalculator.calculate();

        //then
        assertThat(charge.getChargeValue()).isEqualTo(1120);
    }

    @DisplayName("최대 노선 추가요금은 500원이고 거리는 34KM에 일반 요금 테스트")
    @Test
    void calculateNormal() {
        //give
        ChargeCalculator chargeCalculator = new ChargeCalculator(20, 34, 구간목록);

        //when
        Charge charge = chargeCalculator.calculate();

        //then
        assertThat(charge.getChargeValue()).isEqualTo(2250);
    }
}
