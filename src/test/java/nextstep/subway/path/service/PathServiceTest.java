package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.fixture.LineFixture;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.fixture.StationFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @InjectMocks
    private PathService pathService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    public void setUp() {
        강남역 = StationFixture.강남역;
        양재역 = StationFixture.양재역;
        교대역 = StationFixture.교대역;
        남부터미널역 = StationFixture.남부터미널역;

        신분당선 = LineFixture.신분당선;
        이호선 = LineFixture.이호선;
        삼호선 = LineFixture.삼호선;

        신분당선.addToLineStation(강남역, 양재역, 10);
        이호선.addToLineStation(교대역, 강남역, 10);
        삼호선.addToLineStation(교대역, 양재역, 5);
        삼호선.addToLineStation(교대역, 남부터미널역, 3);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    @Test
    void 최단경로조회_예외_출발역과_도착역이_같은_경우() {
        // when
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationRepository.findById(any())).thenReturn(Optional.of(양재역));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathService.findPath(new PathRequest(2L, 2L)))
                .withMessage("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역이 없는 경우 예외 발생")
    @Test
    void 최단경로조회_예외_출발역이_없는_경우_예외_발생() {
        // when
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathService.findPath(new PathRequest(1L, 2L)))
                .withMessageContaining("존재하지 않는 역입니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    @Test
    void 최단경로조회_예외_출발역과_도착역이_연결되어_있지_않은_경우_예외_발생() {
        // given
        Station 동천역 = StationFixture.동천역;

        // when
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(동천역));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathService.findPath(new PathRequest(1L, 2L)))
                .withMessageContaining("출발역과 도착역이 이어진 경로가 없습니다.");
    }

    @AfterEach
    public void after() {
        LineFixture.신분당선.getSections().clear();
        LineFixture.이호선.getSections().clear();
        LineFixture.삼호선.getSections().clear();
    }
}
