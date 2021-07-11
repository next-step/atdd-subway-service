package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ChargePolicyTest {
	@ParameterizedTest(name = "거리가 10km 이하인 경우는 기본 요금 1250원을 반환한다.")
	@ValueSource(ints = {1, 2, 5, 10})
	void chargePolicyApplyTest(int distance) {
		ChargePolicy chargePolicy = ChargePolicy.valueMatchedByDistance(distance);
		assertThat(chargePolicy.getCharge(distance)).isEqualTo(BigDecimal.valueOf(1250));
	}

	@ParameterizedTest(name = "거리가 10km ~ 50km 까지 5km 마다 100원의 추가운임이 부과된다.")
	@CsvSource(value = {"15:1350.0", "20:1450.0", "30:1650.0", "50:2050.0"}, delimiter = ':')
	void extraChargeTest(int distance, double charge) {
		ChargePolicy chargePolicy = ChargePolicy.valueMatchedByDistance(distance);
		assertThat(chargePolicy.getCharge(distance)).isEqualTo(BigDecimal.valueOf(charge));
	}

	@ParameterizedTest(name = "거리가 50km 이상이면 8km 마다 100원의 추가운임이 부과된다.")
	@CsvSource(value = {"58:2150.0", "74:2350.0", "178:3650.0"}, delimiter = ':')
	void extraChargeTest2(int distance, double charge) {
		ChargePolicy chargePolicy = ChargePolicy.valueMatchedByDistance(distance);
		assertThat(chargePolicy.getCharge(distance)).isEqualTo(BigDecimal.valueOf(charge));
	}
}
