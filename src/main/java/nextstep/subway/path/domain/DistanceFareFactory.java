package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.DistanceFarePolicy.*;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Distance;

class DistanceFareFactory {

	private static final Fare DEFAULT_FARE = Fare.from(1250);
	private static final Fare SURCHARGE = Fare.from(100);
	private static final Distance MEDIUM_DISTANCE_ADDITIONAL_CHARGING_STEP = Distance.from(5);
	private static final Distance LONG_DISTANCE_ADDITIONAL_CHARGING_STEP = Distance.from(8);
	private static final String INVALID_MEDIUM_DISTANCE_MESSAGE_FORMAT = "거리는 이전 거리 기준의 최대 거리(%d)보다 커야 합니다.";

	private DistanceFareFactory() {
	}

	static Fare defaultFare() {
		return DEFAULT_FARE;
	}

	static Fare mediumDistanceFare(Distance distance) {
		validDistance(distance, DEFAULT);
		return DEFAULT_FARE.sum(mediumDistanceExtraFare(distance));
	}

	static Fare longDistanceFare(Distance distance) {
		validDistance(distance, MEDIUM);
		return DEFAULT_FARE
			.sum(mediumDistanceExtraFare(MEDIUM.maxDistance()))
			.sum(longDistanceExtraFare(distance));
	}

	static void validDistance(Distance distance, DistanceFarePolicy policy) {
		if (distance.lessThan(policy.maxDistance())) {
			throw new InvalidDataException(
				String.format(INVALID_MEDIUM_DISTANCE_MESSAGE_FORMAT, policy.maxDistance().value()));
		}
	}

	private static Fare mediumDistanceExtraFare(Distance distance) {
		return farePerDistance(distance.subtract(DEFAULT.maxDistance()), MEDIUM_DISTANCE_ADDITIONAL_CHARGING_STEP);
	}

	private static Fare longDistanceExtraFare(Distance distance) {
		return farePerDistance(distance.subtract(MEDIUM.maxDistance()), LONG_DISTANCE_ADDITIONAL_CHARGING_STEP);
	}

	private static Fare farePerDistance(Distance distance, Distance step) {
		return SURCHARGE.multiply(Fare.from(distance.ceilDivide(step)));
	}
}
