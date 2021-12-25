package nextstep.subway.station;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@DisplayName("역들 도메인 테스트")
public class StationsTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		// given
		Stations expected = Stations.of(Arrays.asList(StationTest.노포역, StationTest.다대포해수욕장역));

		// when
		Stations result = Stations.of(Arrays.asList(StationTest.노포역, StationTest.다대포해수욕장역));

		// then
		assertThat(result).isEqualTo(expected);
	}

	@DisplayName("역들이 주어지면, 역을 하나라도 포함하고 있는지 체크한다")
	@Test
	void containsAnyStationTest() {
		// given
		List<Station> stationList = new ArrayList<>();
		stationList.add(StationTest.노포역);
		stationList.add(StationTest.수영역);
		Stations stations = Stations.of(stationList);

		Stations others = Stations.of(Arrays.asList(StationTest.다대포해수욕장역, StationTest.노포역));

		// when
		boolean result = stations.containsAnyStation(others);

		// then
		assertThat(result).isTrue();
	}

}
