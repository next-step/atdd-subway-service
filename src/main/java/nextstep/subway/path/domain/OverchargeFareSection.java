package nextstep.subway.path.domain;

import java.util.Arrays;

public enum OverchargeFareSection {
	FIRST(10, 50, 5, 100, null),
	SECOND(50, Integer.MAX_VALUE, 8, 100, FIRST);

	private final int start;
	private final int end;
	private final int unit;
	private final int fare;
	private final OverchargeFareSection prev;

	OverchargeFareSection(int start, int end, int unit, int fare, OverchargeFareSection prev) {
		this.start = start;
		this.end = end;
		this.unit = unit;
		this.fare = fare;
		this.prev = prev;
	}

	int calculateTotalOverFare(int distance) {
		int totalOverFare = 0;
		totalOverFare += calculateOverFare(distance);

		OverchargeFareSection current = this;
		while (current.prev != null) {
			totalOverFare += current.prev.calculateOverFare(current.prev.end);
			current = current.prev;
		}

		return totalOverFare;
	}

	int calculateOverFare(int distance) {
		int overDistance = distance - start;
		return (int)((Math.ceil((overDistance - 1) / unit) + 1) * fare);
	}

	static boolean contains(int distance) {
		return findBy(distance) != null;
	}

	static OverchargeFareSection findBy(int distance) {
		return Arrays.stream(values())
			.filter(s -> distance > s.start && distance <= s.end)
			.findAny()
			.orElse(null);
	}
}
