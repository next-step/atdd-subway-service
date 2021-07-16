package nextstep.subway.path.domain;

import java.util.List;
import java.util.NoSuchElementException;

import nextstep.subway.line.domain.Line;

public class FarePolicy {

	public static final int BASE_FARE = 1250;
	private final int BASE_LENGTH = 10;
	private final int ADDITIONAL_FARE = 100;
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
		double discountRate = getDiscountRate(age);
		int additionalFare = getAdditionalFare(lines);

		return fareCalculate(distanceFare, discountRate, additionalFare);
	}

	private int getFareByDistance(int distance) {
		if(Distance.FIRST_TARGET.isInRange(distance)) {
			return BASE_FARE;
		}

		if(Distance.SECOND_TARGET.isInRange(distance)) {
			return BASE_FARE + calculateOverFare(distance - BASE_LENGTH, 5);
		}

		return BASE_FARE + calculateOverFare(distance - BASE_LENGTH, 8);
	}

	private int calculateOverFare(int distance, int term) {
		return (int) ((Math.ceil((distance - 1) / term) + 1) * ADDITIONAL_FARE);
	}

	private double getDiscountRate(int age) {
		if(Age.CHILD.isInRange(age)) {
			return Age.CHILD.getDiscountRate();
		}

		if(Age.ADOLESCENT.isInRange(age)) {
			return Age.ADOLESCENT.getDiscountRate();
		}

		return Age.ADULT.getDiscountRate();
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
		return discountRate != Age.ADULT.getDiscountRate();
	}
}
