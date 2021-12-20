package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.InternalServerException;
import nextstep.subway.path.domain.FindShortestPathResult;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Station;

public class FindShortestPathResultTest {
	@Test
	@DisplayName("객체를 생성한다.")
	void findShortestPathResult_constructor_success() {
		List<Station> stations = new ArrayList<>();
		stations.add(StationTest.삼성역);
		stations.add(StationTest.선릉역);
		FindShortestPathResult actual = new FindShortestPathResult(stations, 2);

		assertThat(actual).isEqualTo(new FindShortestPathResult(Arrays.asList(StationTest.삼성역, StationTest.선릉역), 2));
	}

	@Test
	@DisplayName("객체 생성 시 지하철역의 갯수가 2 미만이면 예외")
	void findShortestPathResult_constructor_exception1() {
		List<Station> stations = new ArrayList<>();
		stations.add(StationTest.삼성역);

		assertThatThrownBy(() -> {
			new FindShortestPathResult(stations, 2);
		}).isInstanceOf(InternalServerException.class)
			.hasMessage("최단 경로의 역들의 개수는 2 이상이어야 합니다.");
	}

	@Test
	@DisplayName("객체 생성 시 거리가 1 미만이면 예외")
	void findShortestPathResult_constructor_exception2() {
		List<Station> stations = new ArrayList<>();
		stations.add(StationTest.삼성역);
		stations.add(StationTest.선릉역);

		assertThatThrownBy(() -> {
			new FindShortestPathResult(stations, 0);
		}).isInstanceOf(InternalServerException.class)
			.hasMessage("최단 경로의 거리는 최소 1 이상이어야 합니다.");
	}
}
