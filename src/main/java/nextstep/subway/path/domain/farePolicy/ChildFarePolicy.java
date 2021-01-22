package nextstep.subway.path.domain.farePolicy;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public class ChildFarePolicy implements JuniorFarePolicy{
	private static final int CHILD_LIMIT_AGE = 6;
	private static final int CHILD_MAXIMUM_AGE = 12;
	private static final double CHILD_DISCOUNT_RATE = 0.5;
	private int fare;

	public ChildFarePolicy(int fare) {
		this.fare = fare;
	}

	@Override
	public int apply() {
		return (int)((fare - FarePolicy.DEFAULT_DEDUCT_AMOUNT) * CHILD_DISCOUNT_RATE);

	}

	public static boolean isChild(int age){
		return age >= CHILD_LIMIT_AGE && age <= CHILD_MAXIMUM_AGE;
	}
}
