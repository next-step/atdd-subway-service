package nextstep.subway.path.domain;

import java.util.Comparator;
import java.util.List;

import nextstep.subway.line.domain.Line;

/**
 * @author : byungkyu
 * @date : 2021/01/21
 * @description :
 **/
public class FarePolicy {
	private static int TEN_KILOMETER_DISTANCE = 10;
	private static int FIFTY_KILOMETER_DISTANCE = 50;

	private static int STANDARD_PER_FIVE_KILOMETER = 5;
	private static int STANDARD_PER_EIGHT_KILOMETER = 8;

	private static int DEFAULT_AMOUNT = 1250;
	private static final int DEFAULT_DEDUCT_AMOUNT = 350;

	private static int NOT_LOGIN_AGE = -1;
	private static final int TEENAGER_MINIMUM_AGE = 13;
	private static final int TEENAGER_MAXIMUM_AGE = 18;
	private static final int CHILD_LIMIT_AGE = 6;
	private static final int CHILD_MAXIMUM_AGE = 12;
	private static final double TEENAGER_DISCOUNT_RATE = 0.2;
	private static final double CHILD_DISCOUNT_RATE = 0.5;

	private int distance;
	private int additionalFare;
	private int age;

	public FarePolicy(List<Line> lines, int distance, Integer age) {
		this.additionalFare = getAdditionalFare(lines);
		this.distance = distance;
		this.age = (age == null) ? NOT_LOGIN_AGE : age;
	}

	private int getAdditionalFare(List<Line> lines) {
		return lines.stream()
			.max(Comparator.comparingLong(Line::getAdditionalFare))
			.get().getAdditionalFare();
	}

	public int calculate() {
		int total = DEFAULT_AMOUNT + calculateFare() + additionalFare;;
		if(isTeenager()){
			return saleAmountForJunior(total, TEENAGER_DISCOUNT_RATE);
		}

		if(isChild()){
			return saleAmountForJunior(total, CHILD_DISCOUNT_RATE);
		}
		return total;
	}

	private int saleAmountForJunior(int total, double rate) {
		return (int)((total - DEFAULT_DEDUCT_AMOUNT) * rate);
	}

	private boolean isChild() {
		return age >= CHILD_LIMIT_AGE && age <= CHILD_MAXIMUM_AGE;
	}

	private boolean isTeenager() {
		return age >= TEENAGER_MINIMUM_AGE && age <= TEENAGER_MAXIMUM_AGE;
	}

	private int calculateFare() {
		if (distance > TEN_KILOMETER_DISTANCE && distance <= FIFTY_KILOMETER_DISTANCE) {
			return calculateOverFare(distance - TEN_KILOMETER_DISTANCE, STANDARD_PER_FIVE_KILOMETER);
		}

		if (distance > FIFTY_KILOMETER_DISTANCE) {
			return calculateOverFare((FIFTY_KILOMETER_DISTANCE - TEN_KILOMETER_DISTANCE),STANDARD_PER_FIVE_KILOMETER)
				+  calculateOverFare((distance - FIFTY_KILOMETER_DISTANCE),STANDARD_PER_EIGHT_KILOMETER);
		}
		return 0;
	}

	private int calculateOverFare(int distance, int standard) {
		return (int)((Math.ceil((distance - 1) / standard) + 1) * 100);
	}


}
