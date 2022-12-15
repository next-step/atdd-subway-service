package nextstep.subway.path.application;

import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationService stationService;
    @InjectMocks
    private PathService service;

    private Long memberId = 1L;
    private Long startStationId = 1L;
    private Long targetStationId = 1L;


    @Test
    @DisplayName("출발역과 도착역이 동일할 경우 조회할 수 없음")
    void test1() {
        assertThatThrownBy(() -> service.findShortestPath(memberId, startStationId, targetStationId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 역의 경로를 조회할 경우 조회할 수 없음")
    void test2() {
        given(stationService.findStationById(9999L)).willThrow(RuntimeException.class);
        given(stationService.findStationById(startStationId)).willReturn(new Station(startStationId, "등록된역"));
        assertThatThrownBy(() -> service.findShortestPath(memberId, startStationId, 9999L))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("null 이 입력될 경우 조회할 수 없음")
    void test3() {
        assertThatThrownBy(() -> service.findShortestPath(memberId, null, targetStationId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("음수가 입력될 경우 조회할 수 없음")
    void test4() {
        assertThatThrownBy(() -> service.findShortestPath(memberId, -1L, targetStationId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
