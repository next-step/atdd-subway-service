package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;

public abstract class FarePolicy {
	protected static final int BASE_FARE = 1250;
	protected static final int ADDITIONAL_FARE = 100;
	protected static final int BASE_RANGE = 10;

	protected List<AdditionalFareEdge> edgeList;

	public FarePolicy(List<AdditionalFareEdge> edgeList) {
		this.edgeList = edgeList;
	}

	public abstract int getFare();

	public int getFare(LoginMember loginMember) {
		if (loginMember.isChild()) {
			return (int) (getFare() - ((getFare() - 350) * 0.5));
		}

		if (loginMember.isAdolescent()) {
			return (int) (getFare() - ((getFare() - 350) * 0.2));
		}

		if (loginMember.isAdult()) {
			return getFare();
		}

		return 0;
	}

	protected int calculateOverFare(int distance, int standardLength) {
		return (int) ((Math.ceil((distance - 1) / standardLength) + 1) * ADDITIONAL_FARE);
	}

	protected int getAdditionalFare() {
		return edgeList.stream()
			.mapToInt(AdditionalFareEdge::getAdditionalFare)
			.max()
			.orElse(0);
	}
}
