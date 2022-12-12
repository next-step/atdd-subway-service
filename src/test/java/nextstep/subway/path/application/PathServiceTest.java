package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@DisplayName("지하철 최단 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    /**
     * 서울역-------1호선(30)----동묘앞
     *  /                        /
     *  /                        /
     * 4호선(10)              6호선(10)
     *  /                       /
     *  /                       /
     * 삼각지----6호선(15)----신당역
     */
    private Line 일호선;
    private Line 사호선;
    private Line 육호선;
    private Station 서울역;
    private Station 동묘앞;
    private Station 신당역;
    private Station 삼각지;

    @BeforeEach
    void setUp() {
        서울역 = new Station("서울역");
        동묘앞 = new Station("동묘앞");
        신당역 = new Station("신당역");
        삼각지 = new Station("삼각지역");

        일호선 = new Line("일호선", "blue", 서울역, 동묘앞, 30);
        사호선 = new Line("사호선", "sky-blue", 서울역, 삼각지, 10);
        육호선 = new Line("육호선", "brown", 동묘앞, 신당역, 10);

        육호선.addSection(new Section(육호선, 신당역, 삼각지, 15));
    }

    @InjectMocks
    private PathService pathService;

    @DisplayName("최단 경로 찾기 - 구간: 서울역- 신당역")
    @Test
    void findTheShortestPath() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(서울역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(신당역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 사호선, 육호선));

        PathResponse pathResponse = pathService.findTheShortestPath(1L, 2L);

        assertAll(() -> {
            assertThat(pathResponse.getStations()).hasSize(3);
            assertThat(pathResponse.getDistance()).isEqualTo(25);
        });
    }

}
