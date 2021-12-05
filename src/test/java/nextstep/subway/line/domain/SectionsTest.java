package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

	private final Station 강남역 = new Station("강남역");
	private final Station 판교역 = new Station("판교역");
	private final Station 광교역 = new Station("광교역");
	private final Line 신분당선 = new Line("신분당선", "red");

	@DisplayName("상행->하행 순으로 정렬된 모든 역의 목록 조회")
	@Test
	void getStations() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 판교역, 광교역, 10));
		sections.add(new Section(신분당선, 강남역, 판교역, 10));

		assertThat(sections.getStations()).containsExactly(강남역, 판교역, 광교역);
	}

	@DisplayName("구간이 없을 때 모든 역의 목록 조회 시 비어있어야 함")
	@Test
	void getStations_empty() {
		final Sections sections = new Sections();
		assertThat(sections.getStations().size()).isEqualTo(0);
	}
}
