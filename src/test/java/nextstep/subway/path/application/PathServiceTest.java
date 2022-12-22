package nextstep.subway.path.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
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

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private Station 양재역;
    private Station 교대역;
    private Line 삼호선;

    private List<Line> 전체노션;

    @BeforeEach
    public void setUp() {
        // given
        pathService = new PathService(stationRepository, lineRepository);

        양재역 = new Station(1L, "양재역");
        교대역 = new Station(2L, "교대역");
        삼호선 = new Line("3호선", "bg-orange-600", 양재역, 교대역, 11);

        전체노션 = Arrays.asList(삼호선);
    }

    @Test
    @DisplayName("지하철 노선 구간을 만들고 조회한다")
    void findPath() {

        PathRequest pathRequest = new PathRequest(양재역, 교대역);

        when(stationRepository.findAllById(any())) .thenReturn(Arrays.asList(양재역,교대역));
        when(lineRepository.findAll()).thenReturn(전체노션);

        LoginMember loginMember = new LoginMember(1L,"test@email.com", 19);

        PathResponse pathResponse = pathService.findPath(pathRequest, loginMember);
        assertThat(pathResponse.getDistance()).isEqualTo(11);
        assertThat(pathResponse.getFare()).isEqualTo(1350);
    }
}