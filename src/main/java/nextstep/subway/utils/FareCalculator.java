package nextstep.subway.utils;

import nextstep.subway.fare.domain.AgeDiscountCalculator;
import nextstep.subway.fare.domain.DistanceFareCalculator;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.StationPath;

public class FareCalculator {

	public static Fare getSubwayFare(Distance distance, Fare lineFare, int age) {
		Fare fare = DistanceFareCalculator.getInstance().calculate(distance);
		fare = fare.plus(lineFare);
		return AgeDiscountCalculator.getInstance().discount(fare, age);
	}

	public static Fare getSubwayFare(Lines lines, StationPath path, int age) {
		Distance distance = new Distance(path.getDistance());
		Fare lineFare = lines.getMaxLineFare();
		return getSubwayFare(distance, lineFare, age);
	}

}
