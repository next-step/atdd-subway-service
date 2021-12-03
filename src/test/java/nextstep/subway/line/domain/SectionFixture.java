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

	public static Section 양재역_양재시민의숲역_구간() {
		return Section.of(6L, 양재역(), 양재시민의숲역(), 4);
	}

	public static Section 교대역_강남역_구간() {
		return Section.of(7L, 교대역(), 강남역(), 3);
	}

	public static Section 강남역_역삼역_구간() {
		return Section.of(8L, 강남역(), 역삼역(), 6);
	}

	public static Section 역삼역_선릉역_구간() {
		return Section.of(9L, 역삼역(), 선릉역(), 5);
	}

	public static Section 교대역_남부터미널역() {
		return Section.of(10L, 교대역(), 남부터미널역(), 5);
	}

	public static Section 남부터미널역_양재역() {
		return Section.of(11L, 남부터미널역(), 양재역(), 2);
	}

	public static Section 중앙역_한대앞역() {
		return Section.of(12L, 중앙역(), 한대앞역(), 2);
	}
}
