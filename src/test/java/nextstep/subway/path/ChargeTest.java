package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.path.domain.Charge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChargeTest {

    @DisplayName("거리 10KM 미만 및 노선 추가요금 없는 상황에서의 요금 확인")
    @Test
    void getFare() {
        Charge charge = new Charge(8, 0);
        assertThat(charge.getFare()).isEqualTo(1250);
    }

    @DisplayName("거리 10KM 미만 및 노선 추가요금이 존재하는 상황에서의 요금 확인")
    @Test
    void getFareWithExtraCharge() {
        Charge charge = new Charge(8, 200);
        assertThat(charge.getFare()).isEqualTo(1450);
    }

    @DisplayName("거리 10KM 초과 및 노선 추가요금이 존재하는 상황에서의 요금 확인")
    @Test
    void getFareWithFirstOverCharge() {
        Charge charge = new Charge(34, 200);
        assertThat(charge.getFare()).isEqualTo(1950);
    }

    @DisplayName("거리 50KM 초과 및 노선 추가요금이 존재하는 상황에서의 요금 확인")
    @Test
    void getFareWithSecondOverCharge() {
        Charge charge = new Charge(67, 500);
        assertThat(charge.getFare()).isEqualTo(2850);
    }

    @DisplayName("거리 10KM 초과 및 노선 추가요금이 존재하는 상황에서의 어린이 요금 확인")
    @Test
    void getFareForKids() {
        Charge charge = new Charge(34, 200);
        charge.updateAgeType(10);
        assertThat(charge.getFare()).isEqualTo(800);
    }

    @DisplayName("거리 10KM 초과 및 노선 추가요금이 존재하는 상황에서의 청소년 요금 확인")
    @Test
    void getFareForYouth() {
        Charge charge = new Charge(34, 200);
        charge.updateAgeType(15);
        assertThat(charge.getFare()).isEqualTo(1280);
    }
}
