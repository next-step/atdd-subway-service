package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.domain.adapters.SafeStationForFavoriteAdapter;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SafeStationForFavoriteAdapterTest {
    private SafeStationForFavoriteAdapter safeStationAdapter;

    @Mock
    private StationService stationService;

    @BeforeEach
    void setup() {
        safeStationAdapter = new SafeStationForFavoriteAdapter(stationService);
    }

    @DisplayName("출발지와 도착지 역 모두 존재하는지 여부를 알 수 있다.")
    @ParameterizedTest
    @MethodSource("isExistStationTestResource")
    void isExistStationTest(boolean sourceExist, boolean targetExist, boolean expectedResult) {
        Long sourceId = 1L;
        Long targetId = 2L;
        given(stationService.isExistStation(sourceId)).willReturn(sourceExist);
        given(stationService.isExistStation(targetId)).willReturn(targetExist);

        boolean isExist = safeStationAdapter.isAllExists(sourceId, targetId);

        assertThat(isExist).isEqualTo(expectedResult);
    }
    public static Stream<Arguments> isExistStationTestResource() {
        return Stream.of(
                Arguments.of(true, true, true),
                Arguments.of(true, false, false)
        );
    }
}
