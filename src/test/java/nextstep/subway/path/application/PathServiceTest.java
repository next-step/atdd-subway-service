package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathRepository;
import nextstep.subway.path.domain.SubwayPathSection;
import nextstep.subway.path.domain.SubwayPathSections;
import nextstep.subway.path.domain.SubwayPathStation;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
	@Mock
	private PathRepository pathRepository;
	@Mock
	private PathFinder pathFinder;

	private Station 강남역 = new Station("강남역");
	private Station 양재역 = new Station("양재역");
	private Station 교대역 = new Station("교대역");
	private Station 남부터미널역 = new Station("남부터미널역");

	private Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
	private Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
	private Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

	@BeforeEach
	void setUp() {
		삼호선.addSection(교대역, 남부터미널역, 3);
	}

	@Test
	void findShortestPath() {
		//given
		// sourceId, targetId로 노선의 최단거리 경로를 찾는다.
		// 찾은 경로를 이용해 pathResponse를 응답한다.
		PathService pathService = new PathService(pathRepository, pathFinder);
		when(pathRepository.findSubwayPathSectionAll()).thenReturn(new SubwayPathSections());
		when(pathRepository.findStationById(교대역.getId())).thenReturn(new SubwayPathStation(교대역));
		when(pathRepository.findStationById(교대역.getId())).thenReturn(new SubwayPathStation(양재역));

		//when
		PathResponse pathResponse = pathService.findShortestPath(교대역.getId(), 양재역.getId());

		//then
		assertThat(pathResponse).isNotNull();

	}

}