package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.ActualMember;
import nextstep.subway.auth.domain.AnonymousMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.StationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("경로 관련 기능 테스트 - Mock")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private final Station 구로 = new Station(1L, "구로");
    private final Station 독산 = new Station(2L, "독산");
    private final Station 남구로 = new Station(3L, "남구로");
    private final Station 철산 = new Station(4L, "철산");
    private final Station 가산디지털단지 = new Station(5L, "가산디지털단지");
    private final Line 일호선 = new Line("일호선", "bg-blue-100", 가산디지털단지, 구로, 10);
    private final Line 칠호선 = new Line("칠호선", "bg-dark-green-100", 철산, 가산디지털단지, 3);

    @Mock
    private StationService stationService;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        일호선.addNewSection(독산, 가산디지털단지, 3);
        칠호선.addNewSection(가산디지털단지, 남구로, 6);
    }

    @DisplayName("출발역과 도착역 사이의 최단경로(+ 거리, 요금)를 조회한다.")
    @Test
    void findShortestPath() {

        //given
        when(stationService.findStationById(1L)).thenReturn(독산);
        when(stationService.findStationById(5L)).thenReturn(남구로);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 칠호선));

        PathService pathService = new PathService(lineRepository, stationService);

        //when
        PathResponse anonymousResponse = pathService.findShortestPath(new AnonymousMember(), 1L, 5L);
        PathResponse generalResponse = pathService.findShortestPath(new ActualMember(1L, "general@test.com", 20), 1L, 5L);
        PathResponse teenagerResponse = pathService.findShortestPath(new ActualMember(1L, "teenager@test.com", 17), 1L, 5L);
        PathResponse childResponse = pathService.findShortestPath(new ActualMember(1L, "child@test.com", 9), 1L, 5L);

        //then
        assertAll("익명사용자(비로그인)",
                () -> assertThat(getStationNames(anonymousResponse)).containsExactly("독산", "가산디지털단지", "남구로"),
                () -> assertThat(anonymousResponse.getDistance()).isEqualTo(9),
                () -> assertThat(anonymousResponse.getFare()).isEqualTo(1250)
        );

        assertAll("일반",
                () -> assertThat(getStationNames(generalResponse)).containsExactly("독산", "가산디지털단지", "남구로"),
                () -> assertThat(generalResponse.getDistance()).isEqualTo(9),
                () -> assertThat(generalResponse.getFare()).isEqualTo(1250)
        );

        assertAll("청소년",
                () -> assertThat(getStationNames(teenagerResponse)).containsExactly("독산", "가산디지털단지", "남구로"),
                () -> assertThat(teenagerResponse.getDistance()).isEqualTo(9),
                () -> assertThat(teenagerResponse.getFare()).isEqualTo(720)
        );

        assertAll("어린이",
                () -> assertThat(getStationNames(childResponse)).containsExactly("독산", "가산디지털단지", "남구로"),
                () -> assertThat(childResponse.getDistance()).isEqualTo(9),
                () -> assertThat(childResponse.getFare()).isEqualTo(450)
        );
    }

    private List<String> getStationNames(PathResponse pathResponse) {
        return pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }
}
