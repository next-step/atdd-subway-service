package nextstep.subway.path.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.path.domain.DistanceFarePolicy;

@DisplayName("DistanceFarePolicy 단위테스트")
public class DistanceFarePolicyTest {

	@DisplayName("거리가 0이거나 0보다 작은 경우")
	@Test
	void construct() {
		int distance = 0;

		assertThatIllegalArgumentException().isThrownBy(() -> DistanceFarePolicy.fare(distance)).withMessage("거리가 불명확하여 요금 측정이 불가합니다.");
	}

	@DisplayName("거리별 요금 계산 검증")
	@CsvSource({"1,1250", "10,1250", "11,1350", "16,1450", "51,2150", "56,2150", "59,2250", "67,2350"})
	@ParameterizedTest
	void fare(int distance, int fare) {

		assertThat(DistanceFarePolicy.fare(distance)).isEqualTo(fare);
	}
}
