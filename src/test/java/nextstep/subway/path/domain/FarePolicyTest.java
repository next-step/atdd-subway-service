package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : byungkyu
 * @date : 2021/01/21
 * @description :
 **/
class FarePolicyTest {

	@DisplayName("10km 이하")
	@Test
	void calculateLowerThenTenKilometer(){
		// given
		FarePolicy farePolicy = new FarePolicy(5);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1250);
	}

	@DisplayName("10km 초과 50km까지")
	@Test
	void calculateBetweenTenKilometerAndFiftyKilometer(){
		// given
		FarePolicy farePolicy = new FarePolicy(30);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1850);

	}

	@DisplayName("50km 초과")
	@Test
	void calculateOverFiftyKilometer(){
		// given
		FarePolicy farePolicy = new FarePolicy(80);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(2850);

	}

}