package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.exception.AppException;
import nextstep.subway.member.domain.Age;

@DisplayName("나이 도메인 테스트")
public class AgeTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		// given
		int ageNumber = 19;
		Age age = Age.of(ageNumber);

		// when, then
		assertThat(age).isEqualTo(Age.of(ageNumber));
	}

	@DisplayName("최소값 0 이상 되어야 한다")
	@Test
	void validateTest() {
		// given
		int ageNumber = -1;

		// when, then
		assertThatThrownBy(() -> Age.of(ageNumber))
			.isInstanceOf(AppException.class);
	}
}
