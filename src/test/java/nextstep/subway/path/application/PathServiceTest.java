package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.PathTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class PathServiceTest extends PathTestUtils {

    @Autowired
    protected StationRepository stationRepository;

    @Autowired
    protected SectionRepository sectionRepository;

    @Autowired
    protected LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    private PathService pathService;


    /**
     * (10)
     * 교대역    --- *2호선* ---       강남역
     * |                                |
     * *3호선*(3)                    *신분당선*(10)
     * |                               |
     * 남부터미널역  --- *3호선*(2) ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();
        stationRepository.saveAll(Arrays.asList(교대역, 남부터미널역, 강남역, 양재역));
        lineRepository.saveAll(Arrays.asList(신분당선, 이호선, 삼호선));
        pathService = new PathService(stationService, lineService);
    }

    @Test
    @DisplayName("시작역과 도착역의 최단거리를 구하는 서비스 기능 테스트 : 교대역에서 양재역까지 최단거리")
    void getDijkstraSortestPath() {
        // given
        Station 시작점 = 교대역;
        Station 도착점 = 양재역;
        given(stationService.findById(시작점.getId())).willReturn(stationRepository.findById(시작점.getId()).get());
        given(stationService.findById(도착점.getId())).willReturn(stationRepository.findById(도착점.getId()).get());
        given(stationService.findAll()).willReturn(stationRepository.findAll());
        given(lineService.findAllSection()).willReturn(sectionRepository.findAll());

        // when
        PathResponse response = pathService.findDijkstraPath(시작점.getId(), 도착점.getId());

        // then
        assertThat(response.getStations().size()).isEqualTo(3);
        assertThat(response.getDistance()).isEqualTo(5);
    }
}
