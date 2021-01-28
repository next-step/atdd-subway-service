package nextstep.subway.fare.domain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

@Component
@Order(2)
public class DistanceFarePolicy implements FarePolicy{
	public static int ADDITIONAL_FARE_UNIT = 100;
	public static int FIRST_SECTION_START_DISTANCE = 10;
	public static int FIRST_SECTION_FARE_INTERVAL = 5;
	public static int SECOND_SECTION_START_DISTANCE = 50;
	public static int SECOND_SECTION_FARE_INTERVAL = 8;

	@Override
	public Fare calculate(LoginMember member, Path path) {
		return Fare.from(calculateDistanceFare(path.getDistance()));
	}

	public static long calculateDistanceFare(long distance) {
		long fare = 0;
		if (distance > FIRST_SECTION_START_DISTANCE) {
			long more10Less50Distance =
				Math.min(SECOND_SECTION_START_DISTANCE, distance) - FIRST_SECTION_START_DISTANCE;
			fare += calculateOverFare(more10Less50Distance, FIRST_SECTION_FARE_INTERVAL);
		}
		if (distance > SECOND_SECTION_START_DISTANCE) {
			long more50Distance = distance - SECOND_SECTION_START_DISTANCE;
			fare += calculateOverFare(more50Distance, SECOND_SECTION_FARE_INTERVAL);
		}
		return fare;
	}

	public static long calculateOverFare(long distance, long interval) {
		return (long)((Math.ceil((distance - 1) / interval) + 1) * ADDITIONAL_FARE_UNIT);
	}
}
