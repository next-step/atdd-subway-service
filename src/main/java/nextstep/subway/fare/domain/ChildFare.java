package nextstep.subway.fare.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class ChildFare implements Fare {

	private static final int DEFAULT_FARE = 1_250;
	private static final int DEFAULT_FARE_MAX_DISTANCE = 10;
	private static final int STEP1_FARE_MAX_DISTANCE = 50;
	private static final int OVER_FARE = 100;
	private static final int STEP1_FARE_STANDARD_DISTANCE = 5;
	private static final int STEP2_FARE_STANDARD_DISTANCE = 8;
	private static final int STEP1_RANGE_DISTANCE = 40;
	private static final int DEDUCTIBLE = 350;

	private int fare;
	private int applyFarePercent;

	public ChildFare() {
		this.applyFarePercent = 50;
	}

	@Override
	public void calculateFare(List<Station> path, List<Section> sections, int distance) {
		int fare = calculateStandardFare(path, sections, distance);
		this.fare = (fare - DEDUCTIBLE) * this.applyFarePercent / 100;
	}

	private int calculateStandardFare(List<Station> path, List<Section> sections, int distance) {
		int additionalFare = calculateAdditionalFare(path, sections);

		if (distance <= DEFAULT_FARE_MAX_DISTANCE) {
			return DEFAULT_FARE + additionalFare;
		}

		if (distance <= STEP1_FARE_MAX_DISTANCE) {
			return DEFAULT_FARE + additionalFare + calculateStep1Fare(
				  distance - DEFAULT_FARE_MAX_DISTANCE);
		}

		return DEFAULT_FARE + additionalFare + calculateStep1Fare(STEP1_RANGE_DISTANCE)
			  + calculateStep2Fare(
			  distance - STEP1_FARE_MAX_DISTANCE);
	}

	private int calculateStep1Fare(int distance) {
		return (int) ((Math.ceil((distance - 1) / STEP1_FARE_STANDARD_DISTANCE) + 1) * OVER_FARE);
	}

	private int calculateStep2Fare(int distance) {
		return (int) ((Math.ceil((distance - 1) / STEP2_FARE_STANDARD_DISTANCE) + 1) * OVER_FARE);
	}

	private int calculateAdditionalFare(List<Station> shortestPath, List<Section> sections) {
		int maxAdditionalFare = 0;
		for (int i = 0; i < shortestPath.size() - 1; i++) {
			int currentAdditionalFare = determineMaxAdditionalFare(sections, shortestPath.get(i),
				  shortestPath.get(i + 1));
			maxAdditionalFare = maxAdditionalFare < currentAdditionalFare ? currentAdditionalFare
				  : maxAdditionalFare;
		}

		return maxAdditionalFare;
	}

	private int determineMaxAdditionalFare(List<Section> sections, Station upStation,
		  Station downStation) {
		return sections.stream()
			  .filter(section -> section.isUpStationEquals(upStation)
					&& section.isDownStationEquals(downStation)
			  )
			  .map(Section::getAdditionalFare)
			  .findFirst().orElse(0);
	}

	@Override
	public int getFare() {
		return fare;
	}
}
