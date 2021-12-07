package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;

public class LineFixture {
	public static Line 신분당선_강남역_광교역() {
		return Line.of(1L, "신분당선", "red lighten-1", SectionFixture.강남역_광교역_구간());
	}

	public static Line 신분당선() {
		List<Section> sections = Arrays.asList(
			SectionFixture.강남역_양재역_구간(),
			SectionFixture.양재역_양재시민의숲역_구간());

		return Line.of(2L, "신분당선", "bg-red-600", sections);
	}

	public static Line 이호선() {
		List<Section> sections = Arrays.asList(
			SectionFixture.교대역_강남역_구간(),
			SectionFixture.강남역_역삼역_구간(),
			SectionFixture.역삼역_선릉역_구간());

		return Line.of(3L, "2호선", "bg-green-600", sections);
	}

	public static Line 삼호선() {
		List<Section> sections = Arrays.asList(
			SectionFixture.교대역_남부터미널역_구간(),
			SectionFixture.남부터미널역_양재역_구간());

		return Line.of(4L, "3호선", "bg-orange-600", sections);
	}

	public static Line 사호선() {
		List<Section> sections = Arrays.asList(SectionFixture.중앙역_한대앞역_구간());

		return Line.of(5L, "4호선", "bg-blue-600", sections);
	}
}
