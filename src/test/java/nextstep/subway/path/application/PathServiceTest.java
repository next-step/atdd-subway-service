package nextstep.subway.path.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @InjectMocks
    private StationService stationService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    private Station 양재역;
    private Station 교대역;
    private Line 삼호선;

    private List<Section> 전체구간;

    @BeforeEach
    public void setUp() {
        // given
        pathService = new PathService(stationService, sectionRepository);

        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        삼호선 = new Line("3호선", "bg-orange-600", 양재역, 교대역, 10);

        전체구간 = Arrays.asList(
                new Section(삼호선, 교대역, 양재역, 10)
        );
    }

    @Test
    @DisplayName("지하철 노선 구간을 만들고 조회한다")
    void findPath() {

        PathRequest pathRequest = new PathRequest(1L, 2L);

        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(양재역));
        when(stationRepository.findById(2L)).thenReturn(Optional.ofNullable(교대역));

        when(sectionRepository.findAll()).thenReturn(전체구간);

        PathResponse pathResponse = pathService.findPath(pathRequest);
        assertThat(pathResponse.getDistance()).isEqualTo(10);
    }
}