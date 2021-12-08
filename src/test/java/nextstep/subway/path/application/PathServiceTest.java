package nextstep.subway.path.application;


import static nextstep.subway.path.step.PathFixtures.*;
import static nextstep.subway.path.step.PathAcceptanceStep.최단경로_조회_길이_계산됨;
import static nextstep.subway.path.step.PathAcceptanceStep.최단경로_조회_됨;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.infrastructure.PathSearchImpl;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("경로조회 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        // given
        PathSearch pathSearch = new PathSearchImpl();
        pathService = new PathService(lineRepository, stationService, pathSearch);
        when(lineRepository.findAll()).thenReturn(전체구간());
    }

    @Test
    @DisplayName("`강남 - 남부터미널` 구간 경로조회")
    void 경로조회_케이스1() {
        // when
        when(stationService.findStationById(강남.getId())).thenReturn(강남);
        when(stationService.findStationById(남부터미널.getId())).thenReturn(남부터미널);
        PathResponse pathResponse = pathService.getShortestPath(강남.getId(), 남부터미널.getId());

        // then
        최단경로_조회_됨(pathResponse, Arrays.asList(강남.getName(), 교대.getName(), 남부터미널.getName()));
        최단경로_조회_길이_계산됨(pathResponse, 12);
    }


    @Test
    @DisplayName("`교대 - 양재` 구간 경로조회")
    void 경로조회_케이스2() {
        // when
        when(stationService.findStationById(교대.getId())).thenReturn(교대);
        when(stationService.findStationById(양재.getId())).thenReturn(양재);
        PathResponse pathResponse = pathService.getShortestPath(교대.getId(), 양재.getId());

        // then
        최단경로_조회_됨(pathResponse, Arrays.asList(교대.getName(), 남부터미널.getName(), 양재.getName()));
        최단경로_조회_길이_계산됨(pathResponse, 5);
    }

}
