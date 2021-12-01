package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixture.*;

public class SectionFixture {
	public static Section 강남역_광교역_구간() {
		return Section.of(1L, 강남역(), 광교역(), 20);
	}

	public static Section 강남역_양재역_구간() {
		return Section.of(2L, 강남역(), 양재역(), 2);
	}
}
