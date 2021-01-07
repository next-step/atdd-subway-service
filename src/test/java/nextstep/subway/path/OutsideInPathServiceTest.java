package nextstep.subway.path;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathCalculateException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OutsideInPathServiceTest {

	@InjectMocks
	private PathService pathService;

	@Mock
	private LineRepository lines;

	@Mock
	private StationRepository stations;

	/**
	 *              거리 5
	 * 교대역    --- *2호선* ---   강남역
	 * |                        |
	 * *3호선*                   *신분당선*
	 * 거리 3                     거리 10
	 * |                        |
	 * 남부터미널역  --- *3호선* --- 양재
	 *              거리 2
	 */

	private Station 강남역;
	private Station 남부터미널역;
	private Station 양재역;
	private Station 교대역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	@BeforeEach
	void setUp() {
		강남역 = mockStation(1L, "강남역");
		남부터미널역 = mockStation(2L, "남부터미널역");
		양재역 = mockStation(3L, "양재역");
		교대역 = mockStation(4L, "교대역");

		신분당선 = mockLine("신분당선", Arrays.asList(mockSection(강남역, 양재역, 10)));
		이호선 = mockLine("이호선", Arrays.asList(mockSection(교대역, 강남역, 5)));
		삼호선 = mockLine("삼호선", Arrays.asList(
				mockSection(교대역, 남부터미널역, 3), mockSection(남부터미널역, 양재역, 2)));

		given(lines.findAll()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));
		given(stations.findById(강남역.getId())).willReturn(Optional.of(강남역));
		given(stations.findById(양재역.getId())).willReturn(Optional.of(양재역));
		given(stations.findById(남부터미널역.getId())).willReturn(Optional.of(남부터미널역));
		given(stations.findById(교대역.getId())).willReturn(Optional.of(교대역));
	}

	private Station mockStation(Long id, String name) {
		Station station = mock(Station.class);
		given(station.getId()).willReturn(id);
		given(station.getName()).willReturn(name);
		return station;
	}

	private Line mockLine(String name, List<Section> sections) {
		Line line = mock(Line.class);
		given(line.getName()).willReturn(name);
		given(line.getSections()).willReturn(sections.iterator());
		return line;
	}

	private Section mockSection(Station upStation, Station downStation, int distance) {
		Section section = mock(Section.class);
		given(section.getUpStation()).willReturn(upStation);
		given(section.getDownStation()).willReturn(downStation);
		given(section.getDistance()).willReturn(new Distance(distance));
		return section;
	}

	@Test
	void calculatePath1() {
		// when
		PathRequest pathRequest = new PathRequest(강남역.getId(), 양재역.getId());
		PathResponse pathResponse = pathService.calculatePath(pathRequest);

		// then
		assertThat(pathResponse.getStations())
				.map(StationResponse::getName)
				.containsExactly("강남역", "양재역");
	}

	@Test
	void calculatePath2() {
		// when
		PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());
		PathResponse pathResponse = pathService.calculatePath(pathRequest);

		// then
		assertThat(pathResponse.getStations())
				.map(StationResponse::getName)
				.containsExactly("강남역", "교대역", "남부터미널역");
	}

	@Test
	void calculatePath_NotExistStation() {
		// given
		given(stations.findById(anyLong())).willReturn(Optional.empty());

		// when
		PathRequest pathRequest = new PathRequest(1L, 2L);
		assertThatThrownBy(() -> pathService.calculatePath(pathRequest))
				.isInstanceOf(PathCalculateException.class)
				.hasMessageContaining("존재하지 않는");
	}
}
