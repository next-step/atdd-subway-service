package nextstep.subway.path.domain;

import java.util.List;

public class SecondAdditionalFarePolicy extends FarePolicy {
	private int length;
	private static final int TARGET_LENGTH = 8;
	public static final int TARGET_RANGE = 50;

	public SecondAdditionalFarePolicy(List<AdditionalFareEdge> edgeList, int length) {
		super(edgeList);
		this.length = length;
	}

	public int getFare() {
		return BASE_FARE
			+ calculateOverFare(FirstAdditionalFarePolicy.TARGET_RANGE, FirstAdditionalFarePolicy.TARGET_LENGTH)
			+ calculateOverFare(length - TARGET_RANGE, TARGET_LENGTH)
			+ getAdditionalFare();
	}
}
