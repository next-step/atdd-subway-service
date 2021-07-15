package nextstep.subway.path.domain;

import java.util.List;

public class FarePolicyFactory {
	public static final int SECOND_ADDITIONAL_FARE_TARGET_LENGTH = 50;
	public static final int FIRST_ADDITIONAL_FARE_TARGET_LENGTH1 = 10;

	public static FarePolicy getFarePolicy(List<AdditionalFareEdge> additionalFareEdges, int length) {
		if (length > SECOND_ADDITIONAL_FARE_TARGET_LENGTH) {
			return new SecondAdditionalFarePolicy(additionalFareEdges, length);
		}

		if (length > FIRST_ADDITIONAL_FARE_TARGET_LENGTH1) {
			return new FirstAdditionalFarePolicy(additionalFareEdges, length);
		}

		return new NoAdditionalFarePolicy(additionalFareEdges);
	}
}
