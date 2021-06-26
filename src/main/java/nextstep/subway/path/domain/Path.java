package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.station.domain.Station;

public class Path {
	private static final Fare BASIC_FARE = Fare.wonOf(1250);
	private final List<Station> stations;
	private final List<SectionEdge> sectionEdges;

	Path(List<Station> stations, List<SectionEdge> sectionEdges) {
		this.stations = stations;
		this.sectionEdges = sectionEdges;
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(stations);
	}

	public int sumTotalDistance() {
		return sectionEdges.stream()
			.mapToInt(SectionEdge::getDistance)
			.sum();
	}

	public Fare calculateFare() {
		Fare totalFare = BASIC_FARE;
		totalFare = totalFare.plus(calculateOverDistanceFare());
		return totalFare.plus(calculateLineExtraFare());
	}

	private Fare calculateOverDistanceFare() {
		int totalDistance = sumTotalDistance();
		OverDistanceFareGrade overDistanceFareGrade = OverDistanceFareGrade.of(totalDistance);
		return overDistanceFareGrade.calculateOverFare(totalDistance);
	}

	private Fare calculateLineExtraFare() {
		return sectionEdges.stream()
			.map(SectionEdge::getLineExtraFare)
			.max(Fare::compareTo)
			.orElseThrow(IllegalStateException::new);
	}
}
