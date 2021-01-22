package nextstep.subway.path.domain.farePolicy;

import java.util.function.Function;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public enum JuniorBillingStrategy {
	NONE(fare -> fare),
	CHILD(fare -> (int)((fare - Constants.DEFAULT_DEDUCT_AMOUNT) * 0.5)),
	TEENAGER(fare -> (int)((fare - Constants.DEFAULT_DEDUCT_AMOUNT) * 0.2));

	private static class Constants {
		private static final int DEFAULT_DEDUCT_AMOUNT = 350;

		private static final int CHILD_MINIMUM_AGE = 6;
		private static final int CHILD_MAXIMUM_AGE = 12;

		private static final int TEENAGER_MINIMUM_AGE = 13;
		private static final int TEENAGER_MAXIMUM_AGE = 18;

	}

	private final Function<Integer, Integer> expression;

	public int sale(int fare){
		return expression.apply(fare);
	}

	JuniorBillingStrategy(Function<Integer, Integer> expression) {
		this.expression = expression;
	}

	public static JuniorBillingStrategy of(int age) {
		if(age >= Constants.CHILD_MINIMUM_AGE && Constants.CHILD_MAXIMUM_AGE >= age){
			return CHILD;
		}

		if(age >= Constants.TEENAGER_MINIMUM_AGE && Constants.TEENAGER_MAXIMUM_AGE >= age){
			return TEENAGER;
		}

		return NONE;
	}
}
