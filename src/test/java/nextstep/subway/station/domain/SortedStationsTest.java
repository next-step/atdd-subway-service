package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SortedStationsTest {

	@Test
	void testIsContains() {
		SortedStations sortedStations = new SortedStations();
		sortedStations.addStation(new Station("강남"));
		sortedStations.addStation(new Station("역삼"));

		assertThat(sortedStations.isContains(new Station("강남"))).isTrue();
		assertThat(sortedStations.isContains(new Station("역삼"))).isTrue();
		assertThat(sortedStations.isContains(new Station("잠실"))).isFalse();
	}
}