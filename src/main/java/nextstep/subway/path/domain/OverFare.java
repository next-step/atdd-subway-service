package nextstep.subway.path.domain;

import java.math.BigDecimal;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;

public enum OverFare {

	NONE_OVER_FARE(Distance.of(0), Distance.of(10)){
		public SubwayFare calculate(Distance distance) {
			return SubwayFare.of(BigDecimal.ZERO);
		}
	},
	FIRST_SECTION(Distance.of(10), Distance.of(50)){
		public SubwayFare calculate(Distance distance) {
			Distance firstSectionDistance = distance.decrease(SUBWAY_BASE_FARE_DISTANCE);
			return SubwayFare.of(calculateOverFare(firstSectionDistance, FIRST_SECTION_DIVIDE_VALUE));
		}
	},
	SECOND_SECTION(Distance.of(50), Distance.of(Integer.MAX_VALUE)){
		public SubwayFare calculate(Distance distance) {
			Distance overDistance = distance.decrease(SUBWAY_BASE_FARE_DISTANCE);
			Distance firstSectionDistance = Distance.of(40);
			Distance secondSectionDistance = overDistance.decrease(firstSectionDistance);
			return SubwayFare.of(calculateOverFare(firstSectionDistance, FIRST_SECTION_DIVIDE_VALUE)
				.add(calculateOverFare(secondSectionDistance, SECOND_SECTION_DIVIDE_VALUE)));
		}
	};


	private static final Distance SUBWAY_BASE_FARE_DISTANCE = Distance.of(10);
	private static final int DISTANCE_PER_BASE_OVER_FARE = 100;
	private static final int FIRST_SECTION_DIVIDE_VALUE = 5;
	private static final int SECOND_SECTION_DIVIDE_VALUE = 8;
	private Distance minDistance;
	private Distance maxDistance;


	OverFare(Distance minDistance, Distance maxDistance) {
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}

	public boolean checkGrade(Distance distance){
		return distance.greaterThan(minDistance) && distance.lessThanOrEqual(maxDistance) ;
	}

	public abstract SubwayFare calculate(Distance distance);


	private static BigDecimal calculateOverFare(Distance distance, int divideValue) {
		return BigDecimal.valueOf(Math.ceil(distance.divide(divideValue)) * DISTANCE_PER_BASE_OVER_FARE);
	}
}
