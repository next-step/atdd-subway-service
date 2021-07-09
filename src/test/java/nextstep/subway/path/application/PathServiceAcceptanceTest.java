package nextstep.subway.path.application;

import nextstep.subway.exception.NoPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("최단 경로 서비스 관련 인수 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceAcceptanceTest {

    @Mock
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L,"강남역");
        양재역 = new Station(2L,"양재역");
    }

    @DisplayName("출발역과 도착역이 같으면 실패한다.")
    @Test
    void findBestPathFailBecauseOfSameSourceAndTargetTest() {
        when(pathService.findShortestPath(강남역.getId(), 강남역.getId())).thenThrow(new NoPathException("출발역과 도착역이 같습니다."));

        //when && then
        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 강남역.getId()))
                .isInstanceOf(NoPathException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 실패한다.")
    @Test
    void findBestPathFailBecauseOfNotExistStationTest() {
        //given
        when(pathService.findShortestPath(강남역.getId(), 양재역.getId())).thenThrow(new IllegalArgumentException("등록되지 않은 역입니다."));

        //when && then
        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 양재역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 역입니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않으면 실패한다.")
    @Test
    void findBestPathFailBecauseOfDisconnectedStationTest() {
        //given
        when(pathService.findShortestPath(강남역.getId(), 양재역.getId())).thenThrow(new NoPathException("연결된 경로가 없습니다."));

        //when && then
        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 양재역.getId()))
                .isInstanceOf(NoPathException.class)
                .hasMessageContaining("연결된 경로가 없습니다.");
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void findBestPathTest() {
        //given
        List<Station> stations = Arrays.asList(강남역, 양재역);
        when(pathService.findShortestPath(강남역.getId(),양재역.getId())).thenReturn(new PathResponse(stations,10));

        //when
        PathResponse result = pathService.findShortestPath(강남역.getId(), 양재역.getId());

        //then
        assertThat(result.getStations()).containsExactly(강남역,양재역);
        assertThat(result.getDistance()).isEqualTo(10);
    }

}