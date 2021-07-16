package nextstep.subway.path.domain;

public enum Age {
	CHILD(6, 12, 0.5), ADOLESCENT(13, 18, 0.2), ADULT(19, Integer.MAX_VALUE, 1.0);

	private int minAge;
	private int maxAge;

	private double discountRate;

	Age(int minAge, int maxAge, double discountRate) {
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.discountRate = discountRate;
	}

	public boolean isInRange(int age) {
		return age >= minAge && age <= maxAge;
	}

	public double getDiscountRate() {
		return discountRate;
	}
}
