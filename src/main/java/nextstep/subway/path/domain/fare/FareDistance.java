package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.BiFunction;

import lombok.AllArgsConstructor;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.distance.DefaultDistanceFare;
import nextstep.subway.path.domain.fare.distance.LongDistanceFare;
import nextstep.subway.path.domain.fare.distance.MiddleDistanceFare;

@AllArgsConstructor
public enum FareDistance {
	SHORT_DISTANCE(0, 10, (distance, ageFare) -> {
		return new DefaultDistanceFare().calculate(distance, ageFare);
	}),
	MIDDLE_DISTANCE(11, 50, (distance, ageFare) -> {
		return new MiddleDistanceFare().calculate(distance, ageFare);
	}),
	LONG_DISTANCE(51, Integer.MAX_VALUE, (distance, ageFare) -> {
		return new LongDistanceFare().calculate(distance, ageFare);
	});

	private final int minimumDistance;
	private final int maximumDistance;
	private final BiFunction<Distance, FareUserAge, Money> expression;

	public static Money calculateDistance(final Distance distance, final FareUserAge ageFare) {
		return findDistanceFareType(distance.getDistance()).expression.apply(distance, ageFare);
	}

	private static FareDistance findDistanceFareType(final int distance) {
		return Arrays.stream(values())
			.filter(it -> distance >= it.minimumDistance && distance <= it.maximumDistance)
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}
}
