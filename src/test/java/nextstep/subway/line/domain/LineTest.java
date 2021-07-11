package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.SectionsRemovalInValidSizeException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

class LineTest {

	Line line;
	Station 신도림역;
	Station 잠실역;
	Station 서울대입구역;
	Station 낙성대역;
	Station 사당역;

	@BeforeEach
	void setup() {
		신도림역 = new Station("신도림역");
		잠실역 = new Station("잠실역");

		line = new Line("2호선", "green", 신도림역, 잠실역, 10);
		서울대입구역 = new Station("서울대입구역");
		낙성대역 = new Station("낙성대역");
		사당역 = new Station("사당역");

		line.addStation(사당역, 잠실역, 2);
		line.addStation(서울대입구역, 사당역, 2);
		line.addStation(낙성대역, 서울대입구역, 2);
	}

	@DisplayName("라인에 포함되어 있는 station들을 가져온다.")
	@Test
	void 라인_역_찾기() {
		Stations stations = line.getStations();
		assertThat(stations.isMatchStation(신도림역)).isTrue();
		assertThat(stations.isMatchStation(잠실역)).isTrue();
		assertThat(stations.isMatchStation(서울대입구역)).isTrue();
		assertThat(stations.isMatchStation(낙성대역)).isTrue();
		assertThat(stations.isMatchStation(사당역)).isTrue();
	}

	@DisplayName("라인의 상행선 종점을 찾는다.")
	@Test
	void 상행선_종점_찾기() {
		Station actualStation = line.findUpStation();

		assertThat(actualStation).isEqualTo(신도림역);
	}

	@DisplayName("라인의 역을 제거한다.")
	@Test
	void 라인_역_제거() {
		Stations stations = line.getStations();
		assertThat(stations.isMatchStation(서울대입구역)).isTrue();

		line.removeStation(서울대입구역);

		stations = line.getStations();
		assertThat(stations.isMatchStation(서울대입구역)).isFalse();
	}

	@DisplayName("라인의 구간이 1개 이하인 경우 에러가 발생한다.")
	@Test
	void 라인_구간_1개_이하_에러_발생() {
		line = new Line("2호선", "green", 신도림역, 잠실역, 10);

		assertThrows(SectionsRemovalInValidSizeException.class,
			() -> line.removeStation(잠실역)
		);
	}
}
