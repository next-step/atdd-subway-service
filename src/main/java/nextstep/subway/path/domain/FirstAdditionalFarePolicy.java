package nextstep.subway.path.domain;

import java.util.List;

public class FirstAdditionalFarePolicy extends FarePolicy {
	private int length;
	public static final int TARGET_LENGTH = 5;
	public static final int TARGET_RANGE = 50 - BASE_RANGE;

	public FirstAdditionalFarePolicy(List<AdditionalFareEdge> edgeList, int length) {
		super(edgeList);
		this.length = length;
	}

	public int getFare() {
		return BASE_FARE
			+ calculateOverFare(length - BASE_RANGE, TARGET_LENGTH)
			+ getAdditionalFare();
	}
}
