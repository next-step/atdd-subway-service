package nextstep.subway.line.domain;

public class LineFixture {
	public static Line 신분당선_강남역_광교역() {
		return Line.of(1L, "신분당선", "red lighten-1", SectionFixture.강남역_광교역_구간());
	}
}
