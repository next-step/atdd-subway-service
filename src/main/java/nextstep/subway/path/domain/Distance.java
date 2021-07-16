package nextstep.subway.path.domain;

public enum Distance {
	FIRST_TARGET(0, 10, 5), SECOND_TARGET(10, 50, 8);

	private int minDistance;
	private int maxDistance;
	private int term;

	Distance(int minDistance, int maxDistance, int term) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.term = term;
	}

	public boolean isInRange(int distance) {
		return distance >= minDistance && distance <= maxDistance;
	}
}
