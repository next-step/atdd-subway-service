package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.AgePolicy.*;
import static nextstep.subway.path.domain.DistancePolicy.*;

import java.util.List;
import java.util.NoSuchElementException;

import nextstep.subway.line.domain.Line;

public class FarePolicy {

	private final int DEDUCTIBLE_AMOUNT = 350;

	private int fare;

	public FarePolicy(int distance, int age, List<Line> lines) {
		this.fare = fareCalculate(distance, age, lines);
	}

	public int getFare() {
		return this.fare;
	}

	private int fareCalculate(int distance, int age, List<Line> lines) {
		int distanceFare = getFareByDistance(distance);
		double discountRate = getAgePolicyByAge(age).getDiscountRate();
		int additionalFare = getAdditionalFare(lines);

		return fareCalculate(distanceFare, discountRate, additionalFare);
	}

	private int getAdditionalFare(List<Line> lines) {
		return lines.stream()
			.map(Line::getAdditionalFare)
			.mapToInt(x -> x)
			.max()
			.orElseThrow(NoSuchElementException::new);
	}

	private int fareCalculate(int distanceFare, double discountRate, int additionalFare) {
		if(isDiscount(discountRate)) {
			return (int)((distanceFare + additionalFare - DEDUCTIBLE_AMOUNT) * discountRate);
		}
		return distanceFare + additionalFare;
	}

	private boolean isDiscount(double discountRate) {
		return discountRate != AgePolicy.ADULT.getDiscountRate();
	}
}
