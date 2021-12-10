package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Age;

@DisplayName("단위 테스트 - 가격 도메인")
class PriceTest {

	@DisplayName("가격 도메인 생성 테스트")
	@Test
	void generatePrice() {
		// given // when
		Price price = Price.from();

		// then
		assertThat(price.getPrice()).isEqualTo(Price.BASE_PRICE);
	}

	@DisplayName("거리를 매개변수로한 가격 도메인 생성 테스트")
	@Test
	void generatePriceFromDistance() {
		// given
		Distance distance = Distance.from(10);

		// when
		Price price = Price.from(distance);

		// then
		assertThat(price.getPrice()).isEqualTo(Price.BASE_PRICE);
	}

	@DisplayName("거리에 비례하여 초과요금을 계산하는 메소드 테스트")
	@Test
	void generateExtraPrice() throws Exception {
		// given
		Distance distance = Distance.from(50);
		Method method = Price.class.getDeclaredMethod("generateOverPrice", Distance.class);
		method.setAccessible(true);

		// when
		BigDecimal usingMinPriceRate = (BigDecimal)method.invoke(Price.from(), distance);

		// then
		assertThat(usingMinPriceRate.intValue()).isEqualTo(2250);
	}

	@DisplayName("어린이가 탑승했을때 할인된 최종 요금을 계산하는 메소드 테스트")
	@Test
	void calculateDiscountChild() {
		// given
		Distance distance = Distance.from(10);
		Age age = Age.from(6);
		Price price = Price.from(distance);

		// when
		Price childPrice = price.calculateDiscountAge(age);

		// then
		assertThat(childPrice.getIntPrice()).isEqualTo(450);
	}

	@DisplayName("청소년아 탑승했을때 할인된 최종 요금을 계산하는 메소드 테스트")
	@Test
	void calculateDiscountTeenager() {
		// given
		Distance distance = Distance.from(10);
		Age age = Age.from(13);
		Price price = Price.from(distance);

		// when
		Price childPrice = price.calculateDiscountAge(age);

		// then
		assertThat(childPrice.getIntPrice()).isEqualTo(720);
	}

}
