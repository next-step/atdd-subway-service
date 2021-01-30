package nextstep.subway.fare.domain;

public enum DistanceIntervalStandard {
	FIRST_SECTION(10L, 50L, 5L, 100L),
	SECOND_SECTION(50L, Integer.MAX_VALUE, 8L, 100L);

	public final long over;
	public final long equalOrLess;
	public final long interval;
	public final long unitFare;

	DistanceIntervalStandard(long over, long equalOrLess, long interval, long unitFare) {
		this.over = over;
		this.equalOrLess = equalOrLess;
		this.interval = interval;
		this.unitFare = unitFare;
	}
}
