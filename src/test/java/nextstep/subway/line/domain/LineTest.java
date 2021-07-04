package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.exception.InvalidLineException;
import nextstep.subway.station.domain.Station;

public class LineTest {

	Station 성수역;
	Station 뚝섬역;
	Line 이호선;

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
		이호선 = new Line (1L, "2호선", "초록색", new Fare(1000));
		이호선.addLineStation(new Section(이호선, 성수역, 뚝섬역, new Distance(10)));
	}

	@DisplayName("Line 요금 조회")
	@Test
	void getExtraFee() {
		assertThat(이호선.getFare().value()).isEqualTo(1000);
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

	@DisplayName("Line 수정하기")
	@Test
	void update() {
		이호선.update(new Line("1호선", "파랑색", new Fare(100)));
		assertThat(이호선.getName()).isEqualTo("1호선");
		assertThat(이호선.getColor()).isEqualTo("파랑색");
	}

	@DisplayName("Line 생성 - 이름, 색깔이 빈 값 체크")
	@Test
	void createExceptionTest() {
		assertThatThrownBy(
			() -> { Line line = new Line("", "", new Fare(100)); }
		).isInstanceOf(InvalidLineException.class);
	}

	@DisplayName("Line 수정 - Null Exception 체크")
	@Test
	void updateNullExceptionTest() {
		assertThatThrownBy(
			() -> 이호선.update(null)
		).isInstanceOf(InvalidLineException.class);
	}
}
