package nextstep.subway.path.domain.farePolicy;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public class TeenagerFarePolicy implements JuniorFarePolicy{
	private static final int TEENAGER_MINIMUM_AGE = 13;
	private static final int TEENAGER_MAXIMUM_AGE = 18;
	private static final double TEENAGER_DISCOUNT_RATE = 0.2;
	private int fare;

	public TeenagerFarePolicy(int fare) {
		this.fare = fare;
	}

	@Override
	public int apply() {
		return (int)((fare - FarePolicy.DEFAULT_DEDUCT_AMOUNT) * TEENAGER_DISCOUNT_RATE);

	}

	public static boolean isTeenager(int age){
		return age >= TEENAGER_MINIMUM_AGE && age <= TEENAGER_MAXIMUM_AGE;
	}
}
