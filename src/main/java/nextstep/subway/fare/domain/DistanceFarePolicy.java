package nextstep.subway.fare.domain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

@Component
@Order(2)
public class DistanceFarePolicy implements FarePolicy {

	public static long getTotalDistanceFare(long distance) {
		long sum = 0;
		for (DistanceIntervalStandard standard : DistanceIntervalStandard.values()) {
			sum += getDistanceFare(standard, distance);
		}
		return sum;
	}

	public static long getDistanceFare(DistanceIntervalStandard standard, long distance) {
		if (distance <= standard.over) {
			return 0;
		}
		long limit = Math.min(distance, standard.equalOrLess);
		long targetDistance = limit - standard.over;
		return calculateOverFare(targetDistance, standard.interval, standard.unitFare);
	}

	public static long calculateOverFare(long distance, long interval, long unitFare) {
		return (long)((Math.ceil((distance - 1) / interval) + 1) * unitFare);
	}

	@Override
	public Fare calculate(LoginMember member, Path path) {
		return Fare.from(getTotalDistanceFare(path.getDistance()));
	}
}
