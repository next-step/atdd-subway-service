package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChargeTest {

    @Test
    @DisplayName("기본요금 조회")
    void basicCharge(){
        // given
        int distance = 10;

        // when
        Charge charge = new Charge(distance);

        // then
        assertThat(charge.value()).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과 50km 이하 추가요금 조회 - 5km 당 100원 추가")
    void over10kmCharge(){
        // given
        int distance = 50;

        // when
        Charge charge = new Charge(distance);

        // then
        assertThat(charge.value()).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km 초과 추가요금 조회 - 8km 당 100원 추가")
    void over50kmCharge(){
        // given
        int distance = 82;

        // when
        Charge charge = new Charge(distance);

        // then
        assertThat(charge.value()).isEqualTo(2450);
    }
}
