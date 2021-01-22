package nextstep.subway.path.domain.farePolicy;

import java.util.function.Function;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public enum BillingStrategy {
	SHORT(distance -> Constants.DEFAULT_FARE),
	MIDDLE(distance -> SHORT.expression.apply(Constants.SHORT_MAXIMUM_DISTANCE)
		+ (int)((Math.ceil((distance - Constants.SHORT_MAXIMUM_DISTANCE - 1) / 5) + 1) * 100)),
	LONG(distance -> MIDDLE.expression.apply(Constants.MIDDLE_MAXIMUM_DISTANCE)
		+ (int)((Math.ceil((distance - Constants.MIDDLE_MAXIMUM_DISTANCE - 1) / 8) + 1) * 100));

	public int calculate(int distance) {
		return expression.apply(distance);
	}

	private static class Constants {
		public static final int DEFAULT_FARE = 1250;
		public static final int SHORT_MAXIMUM_DISTANCE = 10;
		public static final int MIDDLE_MAXIMUM_DISTANCE = 50;
	}

	private final Function<Integer, Integer> expression;

	BillingStrategy(Function<Integer, Integer> expression) {
		this.expression = expression;
	}

	public static BillingStrategy of(int distance) {
		if (distance <= Constants.SHORT_MAXIMUM_DISTANCE) {
			return SHORT;
		}

		if (distance <= Constants.MIDDLE_MAXIMUM_DISTANCE) {
			return MIDDLE;
		}

		return LONG;
	}

}
