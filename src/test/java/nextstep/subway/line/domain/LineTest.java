package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("노선 도메인 테스트")
public class LineTest {

	private Station 강남역;
	private Station 광교역;
	private Station 양재역;
	private Station 정자역;
	private Line 신분당선;

	@BeforeEach
	public void setup() {
		강남역 = new Station("강남역");
		광교역 = new Station("광교역");
		양재역 = new Station("양재역");
		정자역 = new Station("정자역");
		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 20);
	}

	@DisplayName("노선 등록")
	@Test
	void createLine() {
		노선_생성됨(신분당선);
		노선에_구간_생성됨(신분당선, 강남역, 광교역);
	}

	@DisplayName("노선에 새로운 구간 등록")
	@Test
	void addLineSection() {
		신분당선.addSection(강남역, 양재역, 3);
		노선에_구간_생성됨(신분당선, 강남역, 양재역);
	}

	public void 노선_생성됨(final Line line) {
		assertThat(line).isNotNull();
	}

	public void 노선에_구간_생성됨(final Line line, Station... stations) {
		assertThat(line.getStations()).contains(stations);
	}

}
