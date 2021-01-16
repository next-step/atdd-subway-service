package nextstep.subway.path.domain.fare;

import java.util.List;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public class FareBuilder {

	public static Money calculateDistance(Distance distance) {
		return DistanceCalculator.apply(distance.getDistance());
	}

	public static Money calculateLineAddFare(Lines lines, List<Station> stations) {
		return lines.maximumLineFare(stations);
	}
}
