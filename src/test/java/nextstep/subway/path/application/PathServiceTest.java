package nextstep.subway.path.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철역_노선에_지하철역_추가;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DisplayName("최단 경로 찾기 관련")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationRepository stationRepository;

    @Mock
    private PathFinder pathFinder;

    @InjectMocks
    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L,"강남역");
        양재역 = new Station(2L,"양재역");
        교대역 = new Station(3L,"교대역");
    }


    @DisplayName("출발역과 도착역이 같으면 실패한다.")
    @Test
    void findBestPathFailBecauseOfSameSourceAndTargetTest() {

        //when && then
        assertThatThrownBy(() -> pathService.findBestPath(강남역.getId(), 강남역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 실패한다.")
    @Test
    void findBestPathFailBecauseOfNotExistStationTest() {
        //given
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> pathService.findBestPath(강남역.getId(), 양재역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 역입니다.");
    }
//
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않으면 실패한다.")
    @Test
    void findBestPathFailBecauseOfDisconnectedStationTest() {
        //given
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.ofNullable(양재역));
        when(pathFinder.getTransferStation(강남역, 양재역)).thenReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> pathService.findBestPath(강남역.getId(), 양재역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("연결된 경로가 없습니다.");
    }

    @DisplayName("최단경로를 조회한다")
    @Test
    void findBestPathTest() {
        //given
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(양재역.getId())).thenReturn(Optional.ofNullable(양재역));
        when(pathFinder.getTransferStation(강남역, 양재역)).thenReturn(Optional.ofNullable(교대역));
        when(pathService.findBestPath(강남역.getId(),양재역.getId())).thenReturn(new PathResponse(Arrays.asList(강남역, 양재역),10));

        //when
        PathResponse result = pathService.findBestPath(강남역.getId(), 양재역.getId());

    }



}