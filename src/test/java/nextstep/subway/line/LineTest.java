package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {
	private Line 신분당선;

	private Station 강남역;
	private Station 광교역;

	@BeforeEach
	void setUp() {
		신분당선 = new Line("신분당선", "red");
		강남역 = new Station(1L, "강남역");
		광교역 = new Station(2L, "광교역");
	}

	@Test
	@DisplayName("라인에 섹션을 추가한다.")
	void addLineStationTest() {
		Station 강남역 = new Station(1L, "강남역");
		Station 광교역 = new Station(2L, "광교역");
		SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 광교역.getId(), 10);
	}
}
