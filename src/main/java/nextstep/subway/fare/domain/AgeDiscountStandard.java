package nextstep.subway.fare.domain;

public enum AgeDiscountStandard {
	BABY(1, 6, 0, 0),
	CHILD(6, 13, 350, 0.5),
	TEENAGER(13, 19, 350, 0.8);

	public final int equalOrMore;
	public final int less;
	public final long deduction;
	public final double discountRate;

	AgeDiscountStandard(int equalOrMore, int less, long deduction, double discountRate) {
		this.equalOrMore = equalOrMore;
		this.less = less;
		this.deduction = deduction;
		this.discountRate = discountRate;
	}
}
