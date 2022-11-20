package nextstep.subway.station.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {

    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private StationService stationService;

    @DisplayName("해당하는 지하철역이 존재하지 않으면, 에러가 발생한다.")
    @Test
    void findByIdThrowError() {
        // given
        when(stationRepository.findById(any()))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stationService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.존재하지_않는_역.getErrorMessage());
    }

}
