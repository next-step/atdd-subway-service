package nextstep.subway.path.domain;

import java.util.List;

public class FarePolicyFactory {
	public static FarePolicy getFarePolicy(List<AdditionalFareEdge> additionalFareEdges, int length) {
		if (length > 50) {
			return new SecondAdditionalFarePolicy(additionalFareEdges, length);
		}

		if (length > 10) {
			return new FirstAdditionalFarePolicy(additionalFareEdges, length);
		}

		return new NoAdditionalFarePolicy(additionalFareEdges);
	}
}
