package nextstep.subway.path.domain;

public enum AgeDiscountPolicy {
	KID(6, 12, 50.0, 350),
	TEENAGER(13, 18, 20.0, 350),
	ADULT(20, 999, 0, 0);

	private int minRange;
	private int maxRange;
	private double discountPoint;
	private int deductedFare;

	AgeDiscountPolicy(int minRange, int maxRange, double discountPoint, int deductedFare) {
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.discountPoint = discountPoint;
		this.deductedFare = deductedFare;
	}

	public int getMinRange() {
		return minRange;
	}

	public int getMaxRange() {
		return maxRange;
	}

	public double getDiscountPoint() {
		return discountPoint;
	}

	public int getDeductedFare() {
		return deductedFare;
	}

	public static AgeDiscountPolicy getAgeDiscountPolicy(int age) {
		for (AgeDiscountPolicy discountPolicy : AgeDiscountPolicy.values()) {
			if (age <= discountPolicy.getMaxRange()) {
				return discountPolicy;
			}
		}
		return AgeDiscountPolicy.ADULT;
	}

}
