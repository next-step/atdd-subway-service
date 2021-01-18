package nextstep.subway.path.domain.fare;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;

public class FareBuilder {

	public static Money calculate(LoginMember loginMember, PathFinder finder) {
		FareUserAge ageFare = FareUserAge.findFareAge(loginMember.getAge());
		Money distanceFare = calculateDistanceFare(finder.distance(), ageFare);
		Money lineAddFare = calculateLineAddFare(finder.getLines(), finder.stations(), ageFare);

		return distanceFare.plus(lineAddFare);
	}

	private static Money calculateDistanceFare(Distance distance, FareUserAge ageFare) {
		return FareDistance.calculateDistance(distance, ageFare);
	}

	private static Money calculateLineAddFare(Lines lines, List<Station> stations, FareUserAge ageFare) {
		return ageFare.discount(lines.maximumLineFare(stations));
	}
}
