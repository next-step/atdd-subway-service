package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LineTest {

	private Station 강남역;
	private Station 역삼역;

	private Line 이호선;

	@BeforeEach
	void setUp() {
		// given
		강남역 = new Station();
		역삼역 = new Station();
		이호선 = new Line("2호선", "bg-red-600", 강남역, 역삼역, 10);
	}

	@DisplayName("지하철 노선에 소속 되어있는 지하철 역 조회 테스트")
	@Test
	void getStationsTest() {
		// when
		assertThat(이호선.getStations())
			.isNotEmpty()
			.containsExactly(강남역, 역삼역);
	}

	@DisplayName("지하철 노선에 새로운 구간 추가 테스트")
	@Test
	void addSectionTest() {
		// given
		Station 신림역 = new Station();
		Station 구디역 = new Station();

		// when
		assertThatCode(() -> {
			이호선.addSection(구디역, 강남역, 10);
			이호선.addSection(구디역, 신림역, 5);
		}).doesNotThrowAnyException();

		// then
		assertThat(이호선.getStations())
			.isNotEmpty()
			.containsExactly(구디역, 신림역, 강남역, 역삼역);
	}

	@DisplayName("지하철 노선에 등록된 구간 삭제 테스트")
	@Test
	void removeSectionTest() {
		// given
		Station 신림역 = new Station();
		이호선.addSection(강남역, 신림역, 5);

		// when
		assertThatCode(() -> 이호선.removeSection(신림역))
			.doesNotThrowAnyException();

		// then
		assertThat(이호선.getStations())
			.isNotEmpty()
			.containsExactly(강남역, 역삼역);
	}
}
