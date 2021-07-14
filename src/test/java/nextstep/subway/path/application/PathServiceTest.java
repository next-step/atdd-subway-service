package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.collect.Lists;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.AgePolicy;
import nextstep.subway.path.exception.DuplicatePathException;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exeption.NotFoundStationException;

@DisplayName("경로 서비스 관련 기능")
@ExtendWith(SpringExtension.class)
class PathServiceTest {

    @MockBean
    private LineRepository lineRepository;
    @MockBean
    private StationRepository stationRepository;

    private Station 강남역, 양재역, 교대역, 남부터미널역;
    private Line 신분당선, 삼호선;

    @BeforeEach
    void setup() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        삼호선 = new Line("삼호선", "bg-yellow-600", 교대역, 남부터미널역, 5);
    }

    @Test
    @DisplayName("경로 찾기 기능 : 정상")
    void findPath() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(신분당선));

        // when
        PathService pathService = new PathService(stationRepository, lineRepository);

        // then
        assertThat(pathService.findPath(1L, 2L, AgePolicy.NONE).getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("경로 찾기 에러 : 출발역과 도착역이 같은 경우")
    void findPathOccurDuplicatePathException() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));

        // when
        PathService pathService = new PathService(stationRepository, lineRepository);

        // then
        assertThatThrownBy(() -> pathService.findPath(1L, 1L, AgePolicy.NONE)).isInstanceOf(DuplicatePathException.class);
    }

    @Test
    @DisplayName("경로 찾기 에러 : 출발역과 도착역이 연결이 되어 있지 않은 경우")
    void findPathOccurNotConnectedPathException() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(교대역));
        when(lineRepository.findAll()).thenReturn(Lists.newArrayList(신분당선, 삼호선));

        // when
        PathService pathService = new PathService(stationRepository, lineRepository);

        // then
        assertThatThrownBy(() -> pathService.findPath(1L, 3L, AgePolicy.NONE)).isInstanceOf(NotConnectedPathException.class);
        assertThatThrownBy(() -> pathService.findPath(3L, 1L, AgePolicy.NONE)).isInstanceOf(NotConnectedPathException.class);
    }

    @Test
    @DisplayName("경로 찾기 에러 : 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void findPathOccurNotFoundStationException() {
        // given
        Station 잠실역 = new Station("잠실역");
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(5L)).thenReturn(Optional.of(잠실역));

        // when
        PathService pathService = new PathService(stationRepository, lineRepository);

        // then
        assertThatThrownBy(() -> pathService.findPath(1L, 5L, AgePolicy.NONE)).isInstanceOf(NotFoundStationException.class);
        assertThatThrownBy(() -> pathService.findPath(5L, 1L, AgePolicy.NONE)).isInstanceOf(NotFoundStationException.class);
    }
}