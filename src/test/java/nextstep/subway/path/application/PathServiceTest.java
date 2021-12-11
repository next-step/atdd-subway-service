package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.StationNotFoundException;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	@InjectMocks
	private PathService pathService;

	@Mock
	private StationRepository stationRepository;

	@Mock
	private LineRepository lineRepository;

	/**
	 * 교대역 ------- *2호선(10)* ------- 강남역
	 * |                        		|
	 * *3호선(3)*              		*신분당선(10)*
	 * |                        		|
	 * 남부터미널역 ---- *3호선(2)* ---- 양재역
	 */
	@DisplayName("지하철 경로 조회")
	@Test
	void findPath() {
		final Station 강남역 = mockStation(1L);
		final Station 양재역 = mockStation(2L);
		final Station 교대역 = mockStation(3L);
		final Station 남부터미널역 = mockStation(4L);
		final Line 신분당선 = mockLine(Arrays.asList(mockSection(강남역, 양재역, 10)));
		final Line 이호선 = mockLine(Arrays.asList(mockSection(강남역, 교대역, 10)));
		final Line 삼호선 = mockLine(Arrays.asList(
			mockSection(양재역, 남부터미널역, 2),
			mockSection(교대역, 남부터미널역, 3)
		));
		given(stationRepository.findByIdIn(any())).willReturn(Arrays.asList(강남역, 남부터미널역));
		given(lineRepository.findAll()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));

		final PathResponse pathResponse = pathService.findPath(강남역.getId(), 남부터미널역.getId());

		final List<Long> actualStationIds = pathResponse.getStations().stream()
			.map(StationResponse::getId).collect(Collectors.toList());
		assertThat(actualStationIds).containsExactly(강남역.getId(), 양재역.getId(), 남부터미널역.getId());
		assertThat(pathResponse.getDistance()).isEqualTo(12d);
	}

	@DisplayName("출발역과 도착역이 동일한 경로 조회시 예외발생")
	@Test
	void findPath_same_station_ids() {
		final Station 강남역 = mockStation(1L);
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> pathService.findPath(강남역.getId(), 강남역.getId()));
	}

	@DisplayName("존재하지 않는 역으로 경로 조회시 예외발생")
	@Test
	void findPath_not_found_station() {
		given(stationRepository.findByIdIn(any())).willReturn(Collections.emptyList());
		assertThatExceptionOfType(StationNotFoundException.class)
			.isThrownBy(() -> pathService.findPath(1L, 2L));
	}

	private Station mockStation(Long id) {
		// @note: PathService.getStation() 에서 station.getId() 호출함에도 불구하고 unnecessary stubbings error 발생.
		// - 그래서 lenient 설정을 예외적으로 허용한다.
		final Station station = mock(Station.class, withSettings().lenient());
		given(station.getId()).willReturn(id);
		return station;
	}

	private Line mockLine(List<Section> sections) {
		final Line line = mock(Line.class);
		given(line.getSections()).willReturn(sections);
		return line;
	}

	private Section mockSection(Station upStation, Station downStation, int distance) {
		final Section section = mock(Section.class);
		given(section.getUpStation()).willReturn(upStation);
		given(section.getDownStation()).willReturn(downStation);
		given(section.getDistance()).willReturn(distance);
		return section;
	}
}
