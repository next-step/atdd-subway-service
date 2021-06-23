package nextstep.subway.line.domain;

import nextstep.subway.exception.CustomException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.exception.CustomExceptionMessage.OVER_DISTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

	private Station 강남역;
	private Station 역삼역;

	private Section 강남_역삼_구간;

	@BeforeEach
	void setUp() {
		강남역 = new Station();
		역삼역 = new Station();

		강남_역삼_구간 = new Section(new Line(), 강남역, 역삼역, 10);
	}


	@DisplayName("연결된 상행선 업데이트 테스트")
	@Test
	void updateUpStationTest() {
		// given
		Station 구디역 = new Station();

		// when
		강남_역삼_구간.updateUpStation(구디역, 5);

		// then
		assertThat(강남_역삼_구간.isMatchUpStation(구디역)).isTrue();
	}

	@DisplayName("연결된 하행선 업데이트 테스트")
	@Test
	void updatedownStationTest() {
		// given
		Station 구디역 = new Station();

		// when
		강남_역삼_구간.updateDownStation(구디역, 5);

		// then
		assertThat(강남_역삼_구간.isMatchDownStation(구디역)).isTrue();
	}

	@DisplayName("기존 거리보다 큰 거리 업데이트 실패 테스트")
	@Test
	void overDistanceTest() {
		// given
		Station 구디역 = new Station();

		// when
		assertThatThrownBy(() -> 강남_역삼_구간.updateUpStation(구디역, 15))
			.isInstanceOf(CustomException.class)
			.hasMessageContaining(OVER_DISTANCE.getMessage());
	}

}
