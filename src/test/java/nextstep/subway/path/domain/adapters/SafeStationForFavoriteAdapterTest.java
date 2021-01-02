package nextstep.subway.path.domain.adapters;

import nextstep.subway.exceptions.EntityNotFoundException;
import nextstep.subway.favorite.domain.adapters.SafeStationForFavoriteAdapter;
import nextstep.subway.favorite.domain.adapters.SafeStationInFavorite;
import nextstep.subway.favorite.domain.excpetions.SafeStationInFavoriteException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.application.exceptions.StationEntityNotFoundException;
import nextstep.subway.station.domain.StationFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SafeStationForFavoriteAdapterTest {
    private SafeStationForFavoriteAdapter safeStationAdapter;

    @Mock
    private StationService  stationService;

    @BeforeEach
    void setup() {
        safeStationAdapter = new SafeStationForFavoriteAdapter(stationService);
    }

    @DisplayName("Id를 기반으로 Station 정보를 안전하게 불러올 수 있다.")
    @Test
    void findAllStationsById() {
        Long targetId = 1L;
        given(stationService.findStationById(targetId)).willReturn(StationFixtures.강남역);

        SafeStationInFavorite safeStationInFavorite = safeStationAdapter.getSafeStationInFavorite(targetId);

        assertThat(safeStationInFavorite).isEqualTo(new SafeStationInFavorite(StationFixtures.강남역));
    }

    @DisplayName("존재하지 않는 지하철 역 정보를 불러올 경우 예외가 발생한다.")
    @Test
    void findAllStationFailWithNotExistStationTest() {
        Long notExistId = 1L;
        given(stationService.findStationById(notExistId)).willThrow(new StationEntityNotFoundException("."));

        assertThatThrownBy(() -> safeStationAdapter.getSafeStationInFavorite(notExistId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Station 도메인에서 알 수 없는 오류 발생시 예외가 발생한다.")
    @Test
    void findAllStationFailTest() {
        Long notExistId = 1L;
        given(stationService.findStationById(notExistId)).willThrow(new RuntimeException());

        assertThatThrownBy(() -> safeStationAdapter.getSafeStationInFavorite(notExistId))
                .isInstanceOf(SafeStationInFavoriteException.class);
    }
}