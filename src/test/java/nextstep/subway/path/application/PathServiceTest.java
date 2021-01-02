package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;

@Transactional
@SpringBootTest
class PathServiceTest {
	public static final int 기본요금 = 1250;
	public static final int 신분당선_추가요금 = 900;
	public static final int 이호선_추가요금 = 500;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	@Autowired
	private PathService pathService;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;

	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널역;
	private Station 잠실역;
	private Line 이호선;
	private Line 삼호선;

	@BeforeEach
	void setup() {
		databaseCleanup.execute();
		강남역 = stationRepository.save(new Station("강남역"));
		양재역 = stationRepository.save(new Station("양재역"));
		교대역 = stationRepository.save(new Station("교대역"));
		남부터미널역 = stationRepository.save(new Station("남부터미널역"));
		잠실역 = stationRepository.save(new Station("잠실역"));
		lineRepository.save(new Line("신분당선", "bg-red-600", 강남역, 양재역, 5, 신분당선_추가요금));
		이호선 = lineRepository.save(new Line("이호선", "bg-red-600", 교대역, 강남역, 5, 이호선_추가요금));
		이호선.addSection(new Section(이호선, 강남역, 잠실역, 3));
		삼호선 = lineRepository.save(new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 0));
		삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
	}

	@DisplayName("getShortestPath 메서드는 출발역, 도착역, 나이 정보를 전달하면 최단 거리, 최단 거리를 지나는 역, 요금 정보를 포함하는 결과 객체를 반환한다.")
	@Test
	void getShortestPath() {
		PathResponse pathResponse = pathService.getShortestPath(강남역.getId(), 남부터미널역.getId(), null);

		assertAll(
			() -> assertThat(pathResponse.getStations())
				.extracting("name")
				.containsExactlyElementsOf(Arrays.asList(강남역.getName(), 양재역.getName(), 남부터미널역.getName())),
			() -> assertThat(pathResponse.getDistance()).isEqualTo(7),
			() -> assertThat(pathResponse.getFare()).isEqualTo(2150)
		);
	}

	@DisplayName("getShortestPath 메서드에서 추가요금 900원인 신분당선과 500원인 2호선을 지나는 양재역과 잠실역 사이의 운임은 기본요금에 900원이 추가된다..")
	@Test
	void getShortestPathExtraFare() {
		PathResponse pathResponse = pathService.getShortestPath(양재역.getId(), 잠실역.getId(), null);

		assertThat(pathResponse.getFare()).isEqualTo(기본요금 + 신분당선_추가요금);
	}

}