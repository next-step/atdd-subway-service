package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("단위 테스트 - 구간 도메인")
class SectionTest {

	private Station upStation;
	private Station downStation;
	private Section section;
	private Line line;

	@BeforeEach
	void setUp() {
		upStation = new Station("강남역");
		downStation = new Station("광교역");
		line = new Line("신분당선", "red");
		section = new Section(line, upStation, downStation, 10);

		line.addSection(section);
	}

	@DisplayName("구간의 상행 종점과 같은 경우 판단하는 테스트")
	@Test
	void isSameUpStation() {
		// given // when
		boolean isSameUpStation = section.isSameUpStation(upStation);

		// then
		assertThat(isSameUpStation).isTrue();
	}

	@DisplayName("구간의 하행 종점과 같은 경우 판단하는 테스트")
	@Test
	void isSameDownStation() {
		// given // when
		boolean isSameDownStation = section.isSameDownStation(downStation);

		// then
		assertThat(isSameDownStation).isTrue();
	}

	@DisplayName("구간의 상행, 하행 종점과 같은 경우 테스트")
	@Test
	void isSameUpDownStation() {
		// given
		Section expectSection = new Section(new Line(), upStation, downStation, 4);

		// when
		boolean isSameUpDownStation = section.isSameUpDownStation(expectSection);

		// then
		assertThat(isSameUpDownStation).isTrue();
	}

	@DisplayName("구간의 상, 하행 종점 중 하나라도 같은 경우 테스트")
	@Test
	void isInUpDownStation() {
		// given
		Section expectSection = new Section(new Line(), upStation, downStation, 4);
		Station expectStation = new Station("테스트역");

		// when
		boolean isInUpStation = section.isInUpDownStation(expectSection.getUpStation());
		boolean isInDownStation = section.isInUpDownStation(expectSection.getDownStation());
		boolean isNotInStation = section.isInUpDownStation(expectStation);

		// then
		assertAll(
			() -> assertThat(isInUpStation).isTrue(),
			() -> assertThat(isInDownStation).isTrue(),
			() -> assertThat(isNotInStation).isFalse()
		);
	}

	@DisplayName("상행 종점이 구간 상, 하행 종점 사이에 존재할 때 구간을 재설정하는 테스트")
	@Test
	void upStationBetweenStations() {
		// given
		Station expectUpStation = new Station("강남역");
		Station expectDownStation = new Station("양재역");
		Section expectSection = new Section(line, expectUpStation, expectDownStation, 2);

		// when
		section.reSettingSection(expectSection);

		// then
		assertAll(
			() -> assertThat(section.getDistance())
				.isEqualTo(10 - expectSection.getDistance()),
			() -> assertThat(section.getUpStation()).isEqualTo(expectSection.getDownStation())
		);
	}

	@DisplayName("하행 종점이 구간 상, 하행 종점 사이에 존재할 때 구간을 재설정하는 테스트")
	@Test
	void downStationBetweenStations() {
		// given
		Station expectUpStation = new Station("정자역");
		Station expectDownStation = new Station("광교역");
		Section expectSection = new Section(line, expectUpStation, expectDownStation, 3);

		// when
		section.reSettingSection(expectSection);

		// then
		assertAll(
			() -> assertThat(section.getDistance())
				.isEqualTo(10 - expectSection.getDistance()),
			() -> assertThat(section.getDownStation()).isEqualTo(expectSection.getUpStation())
		);
	}

	@DisplayName("종점 사이의 역을 지울 때 구간을 재설정하는 테스트")
	@Test
	void removeStationBetweenStations() {
		// given
		Station expectUpStation = new Station("정자역");
		Station expectDownStation = new Station("광교역");
		Section expectSection = new Section(line, expectUpStation, expectDownStation, 3);
		section.reSettingSection(expectSection);

		// when
		section.plusDistance(expectSection);

		// then
		assertThat(section.getDistance()).isEqualTo(10);
	}
}