package nextstep.subway.path.domain.adapters;

import nextstep.subway.path.domain.SafeStationInfo;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SafeStationForFavoriteAdapterTest {
    private SafeStationAdapter safeStationAdapter;

    @Mock
    private StationService  stationService;

    @BeforeEach
    void setup() {
        safeStationAdapter = new SafeStationAdapter(stationService);
    }

    @DisplayName("Id를 기반으로 Station 정보를 안전하게 불러올 수 있다.")
    @Test
    void findAllStationsById() {
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L);
        given(stationService.findAllStationsByIds(stationIds)).willReturn(
                Arrays.asList(StationFixtures.강남역, StationFixtures.역삼역, StationFixtures.잠실역));

        List<SafeStationInfo> safeStationInfos = safeStationAdapter.findStationsById(stationIds);

        assertThat(safeStationInfos).contains(
                new SafeStationInfo(1L, "강남역", null),
                new SafeStationInfo(2L, "역삼역", null),
                new SafeStationInfo(3L, "잠실역", null)
        );
    }
}