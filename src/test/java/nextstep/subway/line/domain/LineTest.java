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
}