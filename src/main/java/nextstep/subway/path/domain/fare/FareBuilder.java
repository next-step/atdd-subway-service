package nextstep.subway.path.domain.fare;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;

public class FareBuilder {

	public static Money calculate(LoginMember loginMember, PathFinder finder) {
		FareAge fareAge = FareAge.findFareAge(loginMember.getAge());
		Money distanceFare = calculateDistance(finder.distance(), fareAge);
		Money lineAddFare = calculateLineAddFare(finder.getLines(), finder.stations()).multi(fareAge.getDiscount());

		return distanceFare.plus(lineAddFare);
	}

	public static Money calculateDistance(Distance distance, FareAge fareAge) {
		return DistanceCalculator.apply(distance.getDistance(), fareAge);
	}

	public static Money calculateLineAddFare(Lines lines, List<Station> stations) {
		return lines.maximumLineFare(stations);
	}
}
