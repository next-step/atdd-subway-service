package nextstep.subway.line.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("구간 테스트")
class SectionTest {

	@Test
	@DisplayName("구간 생성")
	void createSectionTest() {
		assertDoesNotThrow(
			() -> Section.of(null, Station.from("강남역"), Station.from("역삼역"), Distance.from(Integer.MAX_VALUE)));
	}

	@Test
	@DisplayName("구간 생성 - 상행역이 null인 경우 예외")
	void createSectionWithoutUpStationTest() {
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, null, Station.from("역삼역"), Distance.from(Integer.MAX_VALUE)));
	}

	@Test
	@DisplayName("구간 생성 - 하행역이 null인 경우 예외")
	void createSectionWithoutDownStationTest() {
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, Station.from("강남역"), null, Distance.from(Integer.MAX_VALUE)));
	}

	@Test
	@DisplayName("구간 생성 - 거리가 null인 경우 예외")
	void createSectionWithoutDistanceTest() {
		assertThrows(IllegalArgumentException.class,
			() -> Section.of(null, Station.from("강남역"), Station.from("역삼역"), null));
	}

}