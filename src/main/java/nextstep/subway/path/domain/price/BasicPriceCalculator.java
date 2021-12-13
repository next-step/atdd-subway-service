package nextstep.subway.path.domain.price;

import java.util.Arrays;
import java.util.Optional;

public class BasicPriceCalculator implements PriceCalculator {

	@Override
	public int calculatePrice(int pathDistance) {
		if (pathDistance <= 0) {
			return 0;
		}

		PathPricePolicy[] policies = PathPricePolicy.values();

		/* 각 구간별 금액 구하기 */
		int price = Arrays.stream(policies)
			.mapToInt(policy -> policy.calculateSectionPrice(pathDistance))
			.sum();

		return price;
	}

	@Override
	public int adjustAgeDiscount(int price, Integer age) {
		Optional<DiscountPolicy> discountType = DiscountPolicy.findDiscountType(age);
		if (!discountType.isPresent()) {
			return price;
		}

		DiscountPolicy discountPolicy = discountType.get();
		return (int)Math.round((price - discountPolicy.getDeduction()) * discountPolicy.getAdaptDiscountRate());
	}
}