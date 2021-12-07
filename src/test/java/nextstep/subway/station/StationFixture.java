package nextstep.subway.station;

import nextstep.subway.station.domain.Station;

public class StationFixture {
	public static final Long UNKNOWN_ID = 0L;

	public static Station 존재하지않는역() {
		return Station.of(UNKNOWN_ID, "존재하지않는역");
	}

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

	public static Station 양재시민의숲역() {
		return Station.of(5L, "양재시민의숲역");
	}

	public static Station 교대역() {
		return Station.of(6L, "교대역");
	}

	public static Station 역삼역() {
		return Station.of(7L, "역삼역");
	}

	public static Station 선릉역() {
		return Station.of(8L, "선릉역");
	}

	public static Station 남부터미널역() {
		return Station.of(9L, "남부터미널역");
	}

	public static Station 중앙역() {
		return Station.of(10L, "중앙역");
	}

	public static Station 한대앞역() {
		return Station.of(11L, "한대앞역");
	}
}
