package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class ShortestPath {
	private List<Station> stations;
	private long distance;
	private int fare;
	private int maxLineOverFare;

	private static final int MINIMUM_FARE = 1250;
	private static final int OVER_FARE = 100;
	private static final long MEDIUM_DISTANCE_CRITERIA = 10;
	private static final long LONG_DISTANCE_CRITERIA = 50;
	private static final long MEDIUM_DISTANCE_CHARGING_CRITERIA = 5;
	private static final long LONG_DISTANCE_CHARGING_CRITERIA = 8;

	protected ShortestPath() {
	}

	public ShortestPath(List<Station> stations, long distance, int maxLineOverFare) {
		this.stations = stations;
		this.distance = distance;
		this.maxLineOverFare = maxLineOverFare;
		this.fare = calculateFare();
	}

	public List<Station> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}

	public int getFare() {
		return fare;
	}

	private int calculateFare() {
		int baseFare = MINIMUM_FARE + maxLineOverFare;
		if (isMediumDistance()) {
			return baseFare + calculateMediumDistanceOverFare();
		}
		if (isLongDistance()) {
			return baseFare + calculateLongDistanceFare();
		}
		return baseFare;
	}

	private boolean isMediumDistance() {
		return distance > MEDIUM_DISTANCE_CRITERIA && distance <= LONG_DISTANCE_CRITERIA;
	}

	private boolean isLongDistance() {
		return distance > LONG_DISTANCE_CRITERIA;
	}

	private int calculateMediumDistanceOverFare() {
		return calculateAdditionalFare(distance - MEDIUM_DISTANCE_CRITERIA, MEDIUM_DISTANCE_CHARGING_CRITERIA);
	}

	private int calculateLongDistanceFare() {
		return calculateMediumDistanceFullOverFare()
			+ calculateAdditionalFare(distance - LONG_DISTANCE_CRITERIA, LONG_DISTANCE_CHARGING_CRITERIA);
	}

	private int calculateMediumDistanceFullOverFare() {
		return calculateAdditionalFare(LONG_DISTANCE_CRITERIA - MEDIUM_DISTANCE_CRITERIA, MEDIUM_DISTANCE_CHARGING_CRITERIA);
	}

	private int calculateAdditionalFare(long overDistance, long chargingCriteria) {
		return (int) ((Math.ceil((overDistance - 1) / chargingCriteria) + 1) * OVER_FARE);
	}
}
