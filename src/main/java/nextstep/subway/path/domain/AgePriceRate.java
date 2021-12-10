package nextstep.subway.path.domain;

import java.math.BigDecimal;

public enum AgePriceRate {

	CHILD(5, 13, 350, 0.5),
	TEENAGER(12, 19, 350, 0.8);

	private final int minAge;
	private final int maxAge;
	private final int deductionPrice;
	private final double discountRate;

	AgePriceRate(int minAge, int maxAge, int deductionPrice, double discountRate) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.deductionPrice = deductionPrice;
		this.discountRate = discountRate;
	}

	public int getMinAge() {
		return minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public BigDecimal getDeductionPrice() {
		return new BigDecimal(deductionPrice);
	}

	public BigDecimal getDiscountRate() {
		return new BigDecimal(discountRate);
	}
}
