package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;

@Embeddable
@Access(AccessType.FIELD)
public class SubwayFare {
	public static final BigDecimal SUBWAY_BASE_FARE = new BigDecimal(1250);
	private static final int SUBWAY_BASE_FARE_DISTANCE = 10;
	private static final int DISTANCE_PER_BASE_OVER_FARE = 100;

	private final BigDecimal subwayFare;

	protected SubwayFare() {
		subwayFare = SUBWAY_BASE_FARE;
	}

	private SubwayFare(BigDecimal subwayFare) {
		this.subwayFare = subwayFare;
	}

	public static SubwayFare of(BigDecimal subwayFare) {
		return new SubwayFare(subwayFare);
	}

	public int value() {
		return subwayFare.intValue();
	}

	public SubwayFare calculateDistanceOverFare(Distance distance) {
		if (distance.lessThanOrEqual(Distance.of(SUBWAY_BASE_FARE_DISTANCE))) {
			return this.add(BigDecimal.ZERO);
		}

		Distance overDistance = distance.decrease(Distance.of(SUBWAY_BASE_FARE_DISTANCE));

		if (distance.lessThanOrEqual(Distance.of(50))) {
			return this.add(calculateOverFareTenToFifty(overDistance));
		}

		return this.add(calculateOverFareMoreThenFifty(overDistance));
	}

	private SubwayFare add(BigDecimal overFare) {
		return new SubwayFare(this.subwayFare.add(overFare));
	}

	private BigDecimal calculateOverFareTenToFifty(Distance overDistance) {
		return BigDecimal.valueOf(Math.ceil(overDistance.divide(5)) * DISTANCE_PER_BASE_OVER_FARE);
	}

	private BigDecimal calculateOverFareMoreThenFifty(Distance overDistance) {
		Distance tenToFiftyDistance = Distance.of(40);
		Distance moreThenFiftyDistance = overDistance.decrease(tenToFiftyDistance);
		return BigDecimal.valueOf(Math.ceil(moreThenFiftyDistance.divide(8)) * DISTANCE_PER_BASE_OVER_FARE)
			.add(calculateOverFareTenToFifty(tenToFiftyDistance));
	}

	public SubwayFare calculateLineOverFare(List<Section> allSections, List<SectionEdge> pathEdges) {
		int maxOverFare = allSections.stream()
			.filter(section -> pathEdges.contains(new SectionEdge(section)))
			.mapToInt(section -> section.getLine().getOverFare())
			.max()
			.orElseThrow(() -> new IllegalArgumentException("최단경로 구간이 존재하지 않습니다."));
		return this.add(new BigDecimal(maxOverFare));
	}
}
