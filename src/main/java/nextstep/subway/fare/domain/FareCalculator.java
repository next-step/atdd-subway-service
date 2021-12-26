package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Stations;

public class FareCalculator {

	private static final Fare BASE_FARE = Fare.of(1_250);

	private FareCalculator() {
	}

	public static Fare calculateFare(Distance distance, Sections sections, Stations stations,
		LoginMember member) {
		Lines lines = sections.getLines(stations);
		Fare fare = BASE_FARE
			.add(DistanceChargePolicy.getFare(distance))
			.add(lines.findMostExpensiveLineFare());
		if (member.isNull()) {
			return fare;
		}
		return AgeDiscountPolicy.discountByAge(fare, member.getAge());
	}
}
