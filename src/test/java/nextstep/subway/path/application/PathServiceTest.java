package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    LineService lineService;

    @Mock
    StationService stationService;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;
    Station 공사중인역;

    Line 이호선;
    Line 삼호선;
    Line 신분당선;
    List<Line> lines;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        공사중인역 = new Station("공사중인역");

        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange", 교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);
        신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 10);
        lines = new ArrayList<>();
        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);
    }

    @DisplayName("최단 경로를 구한다")
    @Test
    void findShortestPath() {
        PathRequest request = new PathRequest(4L, 1L);
        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(request.getSource())).willReturn(남부터미널역);
        given(stationService.findStationById(request.getTarget())).willReturn(강남역);
        PathService pathService = new PathService(lineService, stationService);

        PathResponse shortestPath = pathService.findShortestPath(request);

        List<String> stationNames = shortestPath.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(stationNames).containsExactly(남부터미널역.getName(), 양재역.getName(), 강남역.getName()),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(13)
        );
    }

    @DisplayName("출발역과 도착역이 같으면 EX 발생")
    @Test
    void sameSourceAndTarget() {
        PathRequest request = new PathRequest(4L, 4L);
        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(request.getSource())).willReturn(남부터미널역);
        given(stationService.findStationById(request.getTarget())).willReturn(남부터미널역);
        PathService pathService = new PathService(lineService, stationService);

        ThrowingCallable 출발역과_도착역이_같다 = () -> pathService.findShortestPath(request);

        assertThatIllegalArgumentException().isThrownBy(출발역과_도착역이_같다)
                .withMessageContaining("출발역과 도착역이 같을 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않으면 EX 발생")
    @Test
    void notAddedEdge() {
        PathRequest request = new PathRequest(4L, 1L);
        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(request.getSource())).willReturn(남부터미널역);
        given(stationService.findStationById(request.getTarget())).willReturn(공사중인역);
        PathService pathService = new PathService(lineService, stationService);

        ThrowingCallable 출발역과_도착역이_연결되어_있지_않다 = () -> pathService.findShortestPath(request);

        assertThatIllegalArgumentException().isThrownBy(출발역과_도착역이_연결되어_있지_않다)
                .withMessageContaining("출발역과 도착역의 연결정보가 없습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 EX 발생")
    @Test
    void notExistStation() {
        PathRequest request = new PathRequest(4L, 1L);
        given(lineService.findAll()).willReturn(lines);
        given(stationService.findStationById(request.getSource())).willReturn(남부터미널역);
        given(stationService.findStationById(request.getTarget())).willThrow(EntityNotFoundException.class);
        PathService pathService = new PathService(lineService, stationService);

        ThrowingCallable 도착역이_존재하지_않는다 = () -> pathService.findShortestPath(request);

        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(도착역이_존재하지_않는다);
    }
}
