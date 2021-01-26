package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class Fare {
	public static int BASIC_FARE = 1_250;
	public static int ADDITIONAL_FARE_UNIT = 100;
	public static int FIRST_SECTION_START_DISTANCE = 10;
	public static int FIRST_SECTION_FARE_INTERVAL = 5;
	public static int SECOND_SECTION_START_DISTANCE = 50;
	public static int SECOND_SECTION_FARE_INTERVAL = 8;

	private final long fare;

	public Fare(Sections sections, List<Station> stations, long distance) {
		this.fare = BASIC_FARE + calculateFare(sections, stations, distance);
	}

	public static long calculateFare(Sections sections, List<Station> stations, long distance) {
		return sumExtraFareByLine(sections, stations) + calculateDistanceFare(distance);
	}

	public static long sumExtraFareByLine(Sections sections, List<Station> stations) {
		return getLinesByStationsInPath(sections, stations).stream()
			.mapToInt(Line::getExtraFare)
			.sum();
	}

	public static Set<Line> getLinesByStationsInPath(Sections sections, List<Station> stations) {
		if (stations == null || stations.size() < 2) {
			return Collections.EMPTY_SET;
		}

		Set<Line> lines = new HashSet<>();
		Station upStation = stations.get(0);
		for (int i = 1; i < stations.size(); i++) {
			Station downStation = stations.get(i);
			sections.findLineWithMinDistance(upStation, downStation)
				.ifPresent(lines::add);
			upStation = downStation;
		}
		return lines;
	}

	public static long calculateDistanceFare(long distance) {
		long fare = 0;
		if (distance > FIRST_SECTION_START_DISTANCE) {
			long more10Less50Distance = Math.min(SECOND_SECTION_START_DISTANCE, distance) - FIRST_SECTION_START_DISTANCE;
			fare += calculateOverFare(more10Less50Distance, FIRST_SECTION_FARE_INTERVAL);
		}
		if (distance > SECOND_SECTION_START_DISTANCE) {
			long more50Distance = distance - SECOND_SECTION_START_DISTANCE;
			fare += calculateOverFare(more50Distance, SECOND_SECTION_FARE_INTERVAL);
		}
		return fare;
	}

	public static long calculateOverFare(long distance, long interval) {
		return (long)((Math.ceil((distance - 1) / interval) + 1) * ADDITIONAL_FARE_UNIT);
	}

	public long getFare() {
		return fare;
	}
}
