package nextstep.subway.path.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.path.domain.AgeFarePolicy;

public class AgeFarePolicyTest {

	@DisplayName("나이별 요금 정책 검증")
	@CsvSource({"1250,13,720", "1250,6,450", "1250,29,1250"})
	@ParameterizedTest
	void fare(int adultFare, int age, int resultFare) {

		assertThat(AgeFarePolicy.fare(adultFare, age)).isEqualTo(resultFare);
	}
}
