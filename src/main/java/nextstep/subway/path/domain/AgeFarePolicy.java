package nextstep.subway.path.domain;

/**
 * 청소년: 운임에서 350원을 공제한 금액의 20%할인
 * 어린이: 운임에서 350원을 공제한 금액의 50%할인
 * 	- 청소년: 13세 이상~19세 미만
 * 	- 어린이: 6세 이상~ 13세 미만
 */
public class AgeFarePolicy {
	private static final int CHILDREN_START = 6;
	private static final int CHILDREN_END = 13;
	private static final double CHILDREN_DISCOUNT_FARE = 350;
	private static final double CHILDREN_DISCOUNT_PERCENT = 0.5;

	private static final int TEENAGER_START = 13;
	private static final int TEENAGER_END = 19;
	private static final double TEENAGER_DISCOUNT_FARE = 350;
	private static final double TEENAGER_DISCOUNT_PERCENT = 0.2;

	public static int fare(final Integer fare, final Integer age) {
		if (TEENAGER_START <= age && age < TEENAGER_END) {
			return (int)((fare - CHILDREN_DISCOUNT_FARE) * (1 - TEENAGER_DISCOUNT_PERCENT));
		}

		if (CHILDREN_START <= age && age < CHILDREN_END) {
			return (int)((fare - TEENAGER_DISCOUNT_FARE) * (1 - CHILDREN_DISCOUNT_PERCENT));
		}

		return fare;
	}
}
