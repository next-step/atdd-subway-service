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
	private static int STANDARD_PER_EIGHT_KILOMETER = 5;

	private static int DEFAULT_AMOUNT = 1250;
	private int distance;
	private int additionalFare;

	public FarePolicy(List<Line> lines, int distance) {
		this.additionalFare = getAdditionalFare(lines);
		this.distance = distance;
	}

	private int getAdditionalFare(List<Line> lines) {
		return lines.stream()
			.max(Comparator.comparingLong(Line::getAdditionalFare))
			.get().getAdditionalFare();
	}

	public int calculate() {
		return DEFAULT_AMOUNT + calculateFare() + additionalFare;
	}

	private int calculateFare() {
		if (distance > TEN_KILOMETER_DISTANCE && distance <= FIFTY_KILOMETER_DISTANCE) {
			return calculateOverFare(distance, STANDARD_PER_FIVE_KILOMETER);
		}

		if (distance > FIFTY_KILOMETER_DISTANCE) {
			return calculateOverFare(distance, STANDARD_PER_EIGHT_KILOMETER);
		}
		return 0;
	}

	private int calculateOverFare(int distance, int standard) {
		return (int)((Math.ceil((distance - 1) / standard) + 1) * 100);
	}


}
