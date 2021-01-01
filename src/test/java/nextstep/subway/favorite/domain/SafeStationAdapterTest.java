package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.domain.adapters.SafeStationAdapter;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SafeStationAdapterTest {
    private SafeStationAdapter safeStationAdapter;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setup() {
        safeStationAdapter = new SafeStationAdapter(stationService);
    }

    @DisplayName("인자로 전달된 역의 존재 여부를 알 수 있다.")
    @Test
    void isExistStationTest() {
        Long stationId = 1L;
        given(stationService.isExistStation(stationId)).willReturn(true);

        boolean isExist = safeStationAdapter.isExistStation(stationId);

        assertThat(isExist).isTrue();
    }
}
