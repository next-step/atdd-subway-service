package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nextstep.subway.path.domain.PriceRate;

@Embeddable
public class Distance {

	@Column(name = "distance")
	private int distance;

	protected Distance() {

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

	public boolean isUnderEleven() {
		return distance < PriceRate.MIN_RATE.getMinDistance();
	}

	public boolean isUnderFiftyOne() {
		return distance < PriceRate.MAX_RATE.getMinDistance()
			&& distance > PriceRate.MIN_RATE.getMinDistance();
	}

	public int calculateExtraPriceSize() {
		if (isUnderFiftyOne()) {
			return (int)(Math.ceil((distance - 1) / PriceRate.MIN_RATE.getDivideDistance()) + 1);
		}

		return (int)(Math.ceil((distance - 1) / PriceRate.MAX_RATE.getDivideDistance()) + 1);
	}
}
