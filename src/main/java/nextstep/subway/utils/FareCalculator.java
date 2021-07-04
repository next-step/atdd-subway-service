package nextstep.subway.utils;

import java.util.stream.Collectors;

import org.jgrapht.GraphPath;

import nextstep.subway.fare.domain.AgeDiscountCalculator;
import nextstep.subway.fare.domain.DistanceFareCalculator;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.station.domain.Station;

public class FareCalculator {

	public static Fare getSubwayFare(Distance distance, Fare lineFare, int age) {
		Fare fare = DistanceFareCalculator.getInstance().calculate(distance);
		fare = fare.plus(lineFare);
		return AgeDiscountCalculator.getInstance().discount(fare, age);
	}

	public static Fare getSubwayFare(GraphPath<Station, SectionEdge> path, int age) {
		Distance distance = new Distance((int)path.getWeight());
		Lines lines = new Lines(
			path.getEdgeList().stream().map(SectionEdge::getLine).distinct().collect(Collectors.toList()));
		Fare lineFare = lines.getMostExpensiveExtraFeeLine().getFare();
		return getSubwayFare(distance, lineFare, age);
	}

}
