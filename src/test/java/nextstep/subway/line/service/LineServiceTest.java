package nextstep.subway.line.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("지하철 노선 서비스 단위테스트")
@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    private Station 강남역;
    private Station 광교역;
    private Station 역삼역;
    private Line 신분당선;

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        역삼역 = new Station("역삼역");
        신분당선 = new Line("2호선","bg-green-600", 강남역, 광교역, 10);
    }
    
    @Test
    @DisplayName("노선 목록을 조회하면 정상적으로 반한된다.")
    void getStationsTest() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선));
        List<LineResponse> lineResponses = lineService.findLines();
        assertThat(lineResponses.get(0).getName()).isEqualTo(신분당선.getName());
    }
}
