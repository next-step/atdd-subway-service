package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.Message;
import nextstep.subway.station.domain.Station;

class SectionTest {
	private Line 신분당선;
	private Station 강남역;
	private Station 양재역;
	private Station 청계산입구역;
	private Station 판교역;
	private Station 광교역;

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		청계산입구역 = new Station("청계산입구역");
		판교역 = new Station("판교역");
		광교역 = new Station("광교역");
		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 판교역, 10);
	}

	@DisplayName("등록된 구간이 없을시 정상적으로 구간을 생성한다.")
	@Test
	void createSection_WhenEmptySection() {
		Line line = new Line("1호선", "bg-blue-600");
		line.addSection(Section.createSection(line, 강남역, 양재역, 5));

		assertThat(line.getStations().isContains(강남역)).isTrue();
		assertThat(line.getStations().isContains(양재역)).isTrue();
	}

	@DisplayName("연결된 구간 생성시 정상적으로 구간을 생성한다.")
	@Test
	void createSection_WhenValidSection() {
		신분당선.addSection(Section.createSection(신분당선, 강남역, 양재역, 3));

		assertThat(신분당선.getStations().isContains(강남역)).isTrue();
		assertThat(신분당선.getStations().isContains(양재역)).isTrue();
	}

	@DisplayName("이미 등록된 구간 생성시 오류가 발생한다.")
	@Test
	void createSection_ThrowRuntimeException1() {
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> Section.createSection(신분당선, 강남역, 판교역, 3))
			.withMessage(Message.EXIST_SECTION);
	}

	@DisplayName("연결되지 않은 구간 생성시 오류가 발생한다.")
	@Test
	void createSection_ThrowRuntimeException2() {
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> Section.createSection(신분당선, 양재역, 청계산입구역, 3))
			.withMessage(Message.INVALID_SECTION);
	}

	@DisplayName("상행여게 역간 거리보다 먼 거리 입력시 오류가 발생한다.")
	@Test
	void updateUpStation_ThrowRuntimeException() {
		Section section = new Section(신분당선, 양재역, 청계산입구역, 3);

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> section.updateUpStation(강남역, 10))
			.withMessage(Message.PLEASE_ENTER_SHORTER_DISTANCE);
	}

	@DisplayName("하행역에 역간 거리보다 먼 거리 입력시 오류가 발생한다.")
	@Test
	void updateDownStation_ThrowRuntimeException() {
		Section section = new Section(신분당선, 양재역, 청계산입구역, 3);

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> section.updateDownStation(판교역, 10))
			.withMessage(Message.PLEASE_ENTER_SHORTER_DISTANCE);
	}
}