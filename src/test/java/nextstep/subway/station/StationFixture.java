package nextstep.subway.station;

import nextstep.subway.station.domain.Station;

public class StationFixture {
	public static Station 강남역() {
		return Station.of(1L, "강남역");
	}

	public static Station 양재역() {
		return Station.of(2L, "양재역");
	}

	public static Station 정자역() {
		return Station.of(3L, "정자역");
	}

	public static Station 광교역() {
		return Station.of(4L, "광교역");
	}
}
