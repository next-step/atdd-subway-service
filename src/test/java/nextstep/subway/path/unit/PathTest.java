package nextstep.subway.path.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

@DisplayName("Path 단위테스트")
public class PathTest {

	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널역 = new Station("남부터미널역");

		신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "blue", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "black", 교대역, 양재역, 5);
		삼호선.addSection(교대역, 남부터미널역, 3);
	}

	@DisplayName("짧은 경로에 속한 역 목록")
	@Test
	void getShortestStations() {
		Path path = new Path(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역);
		assertThat(path.getShortestStations()).extracting("name").containsExactly("교대역", "남부터미널역", "양재역");
	}

	@DisplayName("짧은 경로의 길이")
	@Test
	void getShortestDistance() {
		Path path = new Path(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역);
		assertThat(path.getShortestDistance()).isEqualTo(5);
	}

	@DisplayName("예외사항 - 출발역과 도착역이 같은 경우")
	@Test
	void 출발역과_도착역이_같은_경우() {
		assertThatIllegalArgumentException().isThrownBy(() -> new Path(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 교대역)).withMessage("출발역과 도착역이 같습니다.");
	}

	@DisplayName("예외사항 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
	@Test
	void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
		Station A1 = new Station("A1");
		Station A2 = new Station("A2");
		Station B1 = new Station("B1");
		Station B2 = new Station("B2");
		Line A_Line = new Line("A", "white", A1, A2, 10);
		Line B_Line = new Line("B", "black", B1, B2, 10);

		Path path = new Path(Arrays.asList(A_Line, B_Line), A1, B2);
		assertThatIllegalArgumentException().isThrownBy(() -> path.getShortestDistance()).withMessage("출발역과 도착역이 연결되어 있지 않습니다.");
	}

	@DisplayName("예외사항 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
	@Test
	void 존재하지_않은_출발역이나_도착역을_조회_할_경우() {
		Station A1 = new Station("A1");
		Station A2 = new Station("A2");

		Station unregistered = new Station("unregistered");

		Line A_Line = new Line("A", "white", A1, A2, 10);
		assertThatIllegalArgumentException().isThrownBy(() -> new Path(Arrays.asList(A_Line), A1, unregistered)).withMessage("출발역 또는 도착역이 존재하지 않습니다.");
	}
}
