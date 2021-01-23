package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {
	private Line 신분당선;
	private Station 강남역;
	private Station 양재역;
	private Station 판교역;
	private Station 광교역;

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		판교역 = new Station("판교역");
		광교역 = new Station("광교역");
		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 판교역, 10);
		신분당선.addSection(new Section(신분당선, 판교역, 광교역, 5));
		신분당선.addSection(new Section(신분당선, 강남역, 양재역, 3));
	}

	@DisplayName("노선의 지하철역 목록을 조회한다.")
	@Test
	void getStations() {
		assertThat(신분당선.getStations().getStations())
			.hasSize(4)
			.containsExactly(강남역, 양재역, 판교역, 광교역);
	}

	@DisplayName("노선의 구간중 상행역이 같은 구간을 찾는다.")
	@Test
	void findSectionByUpStation() {
		Section section = 신분당선.findSectionByUpStation(판교역).orElse(new Section());
		assertThat(section.getUpStation()).isEqualTo(판교역);
	}

	@DisplayName("노선의 구간중 하행역이 같은 구간을 찾는다.")
	@Test
	void findSectionByDownStation() {
		Section section = 신분당선.findSectionByDownStation(판교역).orElse(new Section());
		assertThat(section.getDownStation()).isEqualTo(판교역);
	}

	@DisplayName("노선의 구간중 상행역이 같은 구간을 수정한다.")
	@Test
	void updateUpStation() {
		boolean isUpdated = 신분당선.updateSectionStation(new Section(신분당선, 강남역, 양재역, 3));

		Section section = 신분당선.findSectionByDownStation(판교역).orElse(new Section());

		assertThat(isUpdated).isTrue();
		assertThat(section.getUpStation()).isEqualTo(양재역);
		assertThat(section.getDistance()).isEqualTo(7);
	}

	@DisplayName("노선의 구간중 하역이 같은 구간을 수정한다.")
	@Test
	void updateDownStation() {
		boolean isUpdated = 신분당선.updateSectionStation(new Section(신분당선, 양재역, 판교역, 3));

		Section section = 신분당선.findSectionByUpStation(강남역).orElse(new Section());

		assertThat(isUpdated).isTrue();
		assertThat(section.getDownStation()).isEqualTo(양재역);
		assertThat(section.getDistance()).isEqualTo(7);
	}
}