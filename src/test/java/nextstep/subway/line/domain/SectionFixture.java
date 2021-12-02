package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixture.*;

public class SectionFixture {
	public static Section 강남역_광교역_구간() {
		return Section.of(1L, 강남역(), 광교역(), 20);
	}

	public static Section 강남역_양재역_구간() {
		return Section.of(2L, 강남역(), 양재역(), 2);
	}

	public static Section 양재역_정자역_구간() {
		return Section.of(3L, 양재역(), 정자역(), 8);
	}

	public static Section 정자역_광교역_구간() {
		return Section.of(4L, 정자역(), 광교역(), 10);
	}

	public static Section 강남역_정자역_구간() {
		return Section.of(5L, 강남역(), 정자역(), 10);
	}
}
