package nextstep.subway.path.domain;

import java.util.List;

public class NoAdditionalFarePolicy extends FarePolicy {
	public NoAdditionalFarePolicy(List<AdditionalFareEdge> edgeList) {
		super(edgeList);
	}

	public int getFare() {
		return BASE_FARE + getAdditionalFare();
	}
}
