package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {
	private static final Distance THREE_DISTANCE = new Distance(3);
	private Line 신분당선;
	private Station 강남역;
	private Station 양재역;
	private Station 청계산입구역;
	private Station 판교역;

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		청계산입구역 = new Station("청계산입구역");
		판교역 = new Station("판교역");
		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 판교역, 10);
	}

	@DisplayName("상행역에 역간 거리보다 먼 거리 입력시 오류가 발생한다.")
	@Test
	void updateUpStation_ThrowRuntimeException() {
		Section section = new Section(신분당선, 양재역, 청계산입구역, THREE_DISTANCE);

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> section.updateUpStation(강남역, new Distance(10)))
			.withMessage(Distance.DISTANCE_GREATER_THAN_ZERO);
	}

	@DisplayName("하행역에 역간 거리보다 먼 거리 입력시 오류가 발생한다.")
	@Test
	void updateDownStation_ThrowRuntimeException() {
		Section section = new Section(신분당선, 양재역, 청계산입구역, THREE_DISTANCE);

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> section.updateDownStation(판교역, new Distance(10)))
			.withMessage(Distance.DISTANCE_GREATER_THAN_ZERO);
	}
}