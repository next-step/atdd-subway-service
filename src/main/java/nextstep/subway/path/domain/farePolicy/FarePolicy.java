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
		int fare = BillingStrategy.of(distance).calculate(distance) + additionalFare;
		return JuniorBillingStrategy.of(age).sale(fare);
	}
}
