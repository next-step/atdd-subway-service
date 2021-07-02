package nextstep.subway.favorite.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FavoriteRequest 클래스")
class FavoriteRequestTest {

    @DisplayName("유효한 서로 다른 역 입력시 즐겨찾기 요청 객체를 생성합니다.")
    @ParameterizedTest
    @CsvSource(value = {"1, 2", "2, 3", "3, 4"})
    void createWithValidParameter(Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        assertThat(favoriteRequest).isNotNull();
    }

    @DisplayName("동일한 시작 종료역으로 즐겨찾기 요청 객체 생성시 예외를 던집니다")
    @ParameterizedTest
    @CsvSource(value = {"1, 1", "2, 2", "3, 3"})
    void createWithDuplicateSourceAndTarget(Long source, Long target) {
        assertThatThrownBy(() -> new FavoriteRequest(source, target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시작 종료역 미입력 조회시 예외를 던집니다")
    @NullSource
    @ParameterizedTest
    void createWithNullStationId(Long stationId) {
        assertThatThrownBy(() -> new FavoriteRequest(stationId, stationId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
