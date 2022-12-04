package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import nextstep.subway.ErrorMessage;
import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("노선 서비스 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private LineService lineService;



    @DisplayName("존재하지 않는 역을 삭제하려고 하면 EntityNotFoundException 발생한다.")
    @Test
    void removeLineStation() {
        Line 신분당선 = Line.ofNameAndColor("신분당선", "빨간색");
        // given
        given(lineRepository.findById(2L)).willReturn(Optional.of(신분당선));
        given(stationService.findStationById(1L)).willThrow(new EntityNotFoundException("Station", 1L));

        // then
        assertThatThrownBy(() ->lineService.removeLineStation(2L, 1L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ErrorMessage.notFoundEntity("Station", 1L));
    }

}
