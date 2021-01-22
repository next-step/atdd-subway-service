package nextstep.subway.path.domain.farePolicy;

import java.util.Comparator;
import java.util.List;

import nextstep.subway.line.domain.Line;

/**
 * @author : byungkyu
 * @date : 2021/01/21
 * @description :
 **/
public class FarePolicy {
	private static int DEFAULT_AMOUNT = 1250;
	public static final int DEFAULT_DEDUCT_AMOUNT = 350;

	private static int NOT_LOGIN_AGE = -1;

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
		JuniorFarePolicy farePolicy;
		int total = DEFAULT_AMOUNT + calculateFare() + additionalFare;

		if (TeenagerFarePolicy.isTeenager(age)) {
			farePolicy = new TeenagerFarePolicy(total);
			return farePolicy.apply();
		}

		if (ChildFarePolicy.isChild(age)) {
			farePolicy = new ChildFarePolicy(total);
			return farePolicy.apply();
		}

		return total;
	}

	private int calculateFare() {
		DistanceFarePolicy distanceFarePolicy;

		if (MiddleDistanceFarePolicy.isMiddleDistance(distance)) {
			distanceFarePolicy = new MiddleDistanceFarePolicy(distance);
			return distanceFarePolicy.calculate();
		}

		if (LongDistanceFarePolicy.isLongDistance(distance)) {
			distanceFarePolicy = new LongDistanceFarePolicy(distance);
			return distanceFarePolicy.calculate();
		}

		return 0;
	}

	public static int calculateOverFare(int distance, int standard) {
		return (int)((Math.ceil((distance - 1) / standard) + 1) * 100);
	}

}
