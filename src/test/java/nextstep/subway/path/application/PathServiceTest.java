package nextstep.subway.path.application;

import nextstep.subway.exception.CanNotFoundShortestPathException;
import nextstep.subway.exception.CanNotFoundStationException;
import nextstep.subway.exception.SameSourceTargetStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathsResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    public void setUp() {
        강남역 = initStation("강남역", 1L);
        양재역 = initStation("양재역", 2L);
        교대역 = initStation("교대역", 3L);
        남부터미널역 = initStation("남부터미널역", 4L);
    }

    @Test
    public void findShortestPathTest_최단경로조회_성공() {
        //given
        PathService pathService = new PathService(lineRepository,stationRepository);
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line("신분당선", "bg-red-600", 강남역, 양재역, 10),
                new Line("이호선", "bg-red-600", 교대역, 강남역, 10),
                new Line("삼호선", "bg-red-600", 교대역, 양재역, 5)));

        //when
        PathsResponse pathsResponse = pathService.getDijkstraShortestPath(강남역, 교대역);

        //then
        assertThat(pathsResponse).isNotNull();
    }

    @Test
    public void findShortestPathTest_최단경로조회__출발역_도착역_같은경우_예외() {
        //given
        PathService pathService = new PathService(lineRepository,stationRepository);

        //when-then
        assertThatThrownBy(() -> pathService.findShortestPath(1L, 1L))
        .isInstanceOf(SameSourceTargetStationException.class);
    }

    @Test
    public void findShortestPathTest_최단경로조회__존재하지않는역_예외() {
        //given
        PathService pathService = new PathService(lineRepository,stationRepository);

        //when-then
        assertThatThrownBy(() -> pathService.findShortestPath(1L, 5L))
                .isInstanceOf(CanNotFoundStationException.class);
    }

    @Test
    public void findShortestPathTest_연결되어있지않은경로조회_예외() {
        //given
        Station 경복궁역 = initStation("경복궁역", 5L);
        Station 광화문역 = initStation("광화문역", 6L);

        PathService pathService = new PathService(lineRepository,stationRepository);
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(new Line("신분당선", "bg-red-600", 강남역, 양재역, 10),
                new Line("이호선", "bg-green-600", 교대역, 강남역, 10),
                new Line("삼호선", "bg-yellow-600", 교대역, 양재역, 5),
                new Line("오호선","bg-purple-600", 경복궁역, 광화문역, 4)));

        //when-then
        assertThatThrownBy(() -> pathService.getDijkstraShortestPath(강남역, 경복궁역))
                .isInstanceOf(CanNotFoundShortestPathException.class);
    }

    private Station initStation(String name, Long id) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}