package nextstep.subway.fare.domain;

import org.springframework.stereotype.Component;

import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

@Component
public class DistanceFarePolicy implements FarePolicy {

	@Override
	public Fare calculate(Path path) {
		return Fare.from(getTotalDistanceFare(path.getDistance()));
	}

	private long getTotalDistanceFare(long distance) {
		long sum = 0;
		for (DistanceIntervalStandard standard : DistanceIntervalStandard.values()) {
			sum += getDistanceFare(standard, distance);
		}
		return sum;
	}

	private long getDistanceFare(DistanceIntervalStandard standard, long distance) {
		if (distance <= standard.over) {
			return 0;
		}
		long limit = Math.min(distance, standard.equalOrLess);
		long targetDistance = limit - standard.over;
		return calculateOverFare(targetDistance, standard.interval, standard.unitFare);
	}

	private long calculateOverFare(long distance, long interval, long unitFare) {
		return (long)((Math.ceil((distance - 1) / interval) + 1) * unitFare);
	}
}
