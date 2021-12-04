package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	@Column(name = "distance")
	private int distance;

	private Distance() {

	}

	private Distance(int distance) {
		this.distance = distance;
	}

	public static Distance from(int distance) {
		return new Distance(distance);
	}

	public int getDistance() {
		return distance;
	}

	public void divideDistance(Distance distance) {
		this.distance -= distance.getDistance();
	}

	public void plusDistance(Distance distance) {
		this.distance += distance.getDistance();
	}
}
