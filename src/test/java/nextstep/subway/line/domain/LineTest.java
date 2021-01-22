package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
		final int previousSectionDistance = 신분당선.findSection(강남역, 광교역)
			.orElse(new Section(신분당선, 강남역, 광교역, 20))
			.getDistance();
		final int distance = 3;

		신분당선.addSection(강남역, 양재역, distance);

		노선에_구간_생성됨(신분당선, 강남역, 양재역);
		노선_기존_구간_나눠짐(신분당선, previousSectionDistance, new Section(신분당선, 강남역, 양재역, distance));
	}

	@DisplayName("노선에 지하철역 삭제")
	@Test
	void removeListStation() {
		신분당선.addSection(강남역, 양재역, 3);

		신분당선.removeStation(양재역);

		노선에_지하철역_삭제됨(신분당선, 양재역);
	}

	private void 노선_기존_구간_나눠짐(final Line line, final int previousSectionDistance, final Section section) {

		Section modifiedSection = line.findSection(양재역, 광교역)
			.orElse(null);

		assertAll(
			() -> assertThat(modifiedSection).isNotNull(),
			() -> assertThat(line.getSections()).contains(section),
			() -> assertThat(modifiedSection.getDistance() + section.getDistance())
				.isEqualTo(previousSectionDistance)
		);
	}

	public void 노선_생성됨(final Line line) {
		assertThat(line).isNotNull();
	}

	public void 노선에_구간_생성됨(final Line line, Station... stations) {
		assertThat(line.getStations()).contains(stations);
	}

	public void 노선에_지하철역_삭제됨(final Line line, final Station station) {
		assertThat(line.getStations()).isNotIn(station);
	}

}
