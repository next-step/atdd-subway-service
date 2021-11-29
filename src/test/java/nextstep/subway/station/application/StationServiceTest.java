package nextstep.subway.station.application;

import nextstep.exception.StationNotFoundException;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class StationServiceTest {

    private StationRepository stationRepository;
    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationRepository = mock(StationRepository.class);
        stationService = new StationService(stationRepository);
    }

    @Test
    void findById_역이_존재하지_않으면_에러를_발생한다() {
        // given
        given(stationRepository.findById(1L)).willReturn(Optional.empty());

        // when
        assertThatExceptionOfType(StationNotFoundException.class)
                .isThrownBy(() -> stationService.findById(1L));
    }
}