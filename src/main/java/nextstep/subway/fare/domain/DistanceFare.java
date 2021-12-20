package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFare {

	BASE(
		distanceKm -> (baseMinKmExclusive() < distanceKm && distanceKm <= baseMaxKmInclusive()),
		distanceKm -> 0
	),
	EXTRA(
		distanceKm -> (baseMaxKmInclusive() < distanceKm && distanceKm <= extraMaxKmInclusive()),
		distanceKm -> BASE.calculate.apply(baseMaxKmInclusive())
			+ calculate(distanceKm - baseMaxKmInclusive(), 5, 100)
	),
	OVER_EXTRA(
		distanceKm -> extraMaxKmInclusive() < distanceKm,
		distanceKm -> EXTRA.calculate.apply(extraMaxKmInclusive())
			+ calculate(distanceKm - extraMaxKmInclusive(), 8, 100)
	);

	private final Function<Double, Boolean> condition;
	private final Function<Double, Integer> calculate;

	DistanceFare(Function<Double, Boolean> condition, Function<Double, Integer> calculate) {
		this.condition = condition;
		this.calculate = calculate;
	}

	private static double baseMinKmExclusive() { return 0; }
	private static double baseMaxKmInclusive() { return 10; }
	private static double extraMaxKmInclusive() { return 50; }

	public static Fare calculate(double distanceKm) {
		validate(distanceKm);
		final DistanceFare distanceFare = getDistanceFare(distanceKm);
		return Fare.of(distanceFare.calculate.apply(distanceKm));
	}

	private static void validate(double distanceKm) {
		if (distanceKm <= baseMinKmExclusive()) {
			throw new IllegalArgumentException("운임료를 부과할 거리는 0km 보다 커야합니다.");
		}
	}

	private static DistanceFare getDistanceFare(double distanceKm) {
		return Arrays.stream(values())
			.filter(distanceFare -> distanceFare.condition.apply(distanceKm))
			.findAny()
			.orElseThrow(IllegalArgumentException::new);
	}

	private static int calculate(double distanceKm, double unitKmToCharge, int fareKrw) {
		return (int) ((Math.floor((distanceKm - 1) / unitKmToCharge) + 1) * fareKrw);
	}
}
