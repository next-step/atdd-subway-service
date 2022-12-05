package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;

@DisplayName("구간 테스트")
class SectionTest {

	@Test
	@DisplayName("구간 생성")
	void createSectionTest() {
		assertDoesNotThrow(
			() -> Section.of(null, Station.from(Name.from("강남역")), Station.from(Name.from("역삼역")),
				mock(Distance.class)));
	}

	@Test
	@DisplayName("구간 생성 - 상행역이 null인 경우 예외")
	void createSectionWithoutUpStationTest() {
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, null, mock(Station.class), mock(Distance.class)));
	}

	@Test
	@DisplayName("구간 생성 - 하행역이 null인 경우 예외")
	void createSectionWithoutDownStationTest() {
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, mock(Station.class), null, mock(Distance.class)));
	}

	@Test
	@DisplayName("구간 생성 - 거리가 null인 경우 예외")
	void createSectionWithoutDistanceTest() {
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, mock(Station.class), mock(Station.class), null));
	}

	@Test
	@DisplayName("구간 생성 - 상행역과 하행역이 같은 경우 예외")
	void createSectionWithSameStationTest() {
		Station 강남역 = Station.from(Name.from("강남역"));
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, 강남역, 강남역, mock(Distance.class)));
	}

	@Test
	@DisplayName("같은 상행역")
	void isSameUpStationTest() {
		Station 강남역 = Station.from(Name.from("강남역"));
		Section section = Section.of(null, 강남역, Station.from(Name.from("역삼역")), mock(Distance.class));
		Section other = Section.of(null, 강남역, Station.from(Name.from("삼성역")), mock(Distance.class));
		assertThat(section.isSameUpStation(other)).isTrue();
	}

	@Test
	@DisplayName("같은 하행역")
	void isSameDownStationTest() {
		Station 역삼역 = Station.from(Name.from("역삼역"));
		Section section = Section.of(null, Station.from(Name.from("강남역")), 역삼역, mock(Distance.class));
		Section other = Section.of(null, Station.from(Name.from("삼성역")), 역삼역, mock(Distance.class));
		assertThat(section.isSameDownStation(other)).isTrue();
	}

	@Test
	@DisplayName("구간 연결")
	void connectTest() {
		// given
		Station 강남역 = Station.from(Name.from("강남역"));
		Station 역삼역 = Station.from(Name.from("역삼역"));
		Station 삼성역 = Station.from(Name.from("삼성역"));
		Section section = Section.of(null, 강남역, 역삼역, mock(Distance.class));

		// when
		section.extend(Section.of(null, 역삼역, 삼성역, mock(Distance.class)));

		// then
		assertThat(section.getUpStation()).isEqualTo(강남역);
		assertThat(section.getDownStation()).isEqualTo(삼성역);
	}

	@Test
	@DisplayName("노선 설정")
	void setLineTest() {
		// given
		Section section = Section.of(null,
			Station.from(Name.from("강남역")), Station.from(Name.from("역삼역")), mock(Distance.class));

		// when
		Line mock = mock(Line.class);
		section.updateLine(mock);

		// then
		assertThat(section.getLine()).isEqualTo(mock);
	}

	@Test
	@DisplayName("상행 종점 -> 새로운 구간의 하행 종점으로 교체")
	void updateUpStationTest() {
		// given
		Station 강남역 = Station.from(Name.from("강남역"));
		Station 역삼역 = Station.from(Name.from("역삼역"));
		Station 삼성역 = Station.from(Name.from("삼성역"));
		Section section = Section.of(null, 강남역, 역삼역, mock(Distance.class));
		Section other = Section.of(null, 역삼역, 삼성역, mock(Distance.class));

		// when
		section.replaceUpStation(other);

		// then
		assertThat(section.getUpStation()).isEqualTo(삼성역);
	}

	@Test
	@DisplayName("하행 종점 -> 새로운 구간의 상행 종점으로 교체")
	void updateDownStationTest() {
		// given
		Station 강남역 = Station.from(Name.from("강남역"));
		Station 역삼역 = Station.from(Name.from("역삼역"));
		Station 삼성역 = Station.from(Name.from("삼성역"));
		Section section = Section.of(null, 역삼역, 삼성역, mock(Distance.class));
		Section other = Section.of(null, 강남역, 역삼역, mock(Distance.class));

		// when
		section.replaceDownStation(other);

		// then
		assertThat(section.getDownStation()).isEqualTo(강남역);
	}

}