package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

public class LineTest {

	Station 성수역;
	Station 뚝섬역;
	Line 이호선;

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
		이호선 = new Line (1L, "2호선", "초록색", 성수역, 뚝섬역, new Distance(10));
	}

	@DisplayName("Line 지하철역 조회")
	@Test
	void getStations() {
		List<Station> stations = 이호선.getStations();
		assertThat(stations.get(0).getName()).isEqualTo("성수역");
		assertThat(stations.get(1).getName()).isEqualTo("뚝섬역");
	}

	@DisplayName("Line 구간 추가하기")
	@Test
	void addLineStations() {
		Station 건대입구역 = new Station("건대입구역");
		Section section = new Section(이호선, 뚝섬역, 건대입구역, new Distance(10));
		이호선.addLineStation(section);
		List<Station> stations = 이호선.getStations();
		assertThat(stations.get(0).getName()).isEqualTo("성수역");
		assertThat(stations.get(1).getName()).isEqualTo("뚝섬역");
		assertThat(stations.get(2).getName()).isEqualTo("건대입구역");
	}

	@DisplayName("Line 역 제거하기")
	@Test
	void removeLineStation() {
		Station 건대입구역 = new Station("건대입구역");
		Section section = new Section(이호선, 뚝섬역, 건대입구역, new Distance(10));
		이호선.addLineStation(section);
		이호선.removeLineStation(성수역);
		List<Station> stations = 이호선.getStations();
		assertThat(stations.get(0).getName()).isEqualTo("뚝섬역");
		assertThat(stations.get(1).getName()).isEqualTo("건대입구역");
	}
}
