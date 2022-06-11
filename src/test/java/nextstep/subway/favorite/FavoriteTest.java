package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기에 대한 단위 테스트")
class FavoriteTest {
    private Station 대림역;
    private Station 구로디지털단지역;

    @BeforeEach
    void setUp() {
        대림역 = new Station("대림역");
        구로디지털단지역 = new Station("구로디지털단지");
    }

    @DisplayName("즐겨찾기에 추가할 지하철역을 전달하면 즐겨찾기 객체에 저장되어야 한다")
    @Test
    void favorite_add_test() {
        // given
        Favorite favorite = Favorite.of(대림역, 구로디지털단지역);

        // then
        assertAll(
            () -> assertThat(favorite.getSource()).isEqualTo(대림역),
            () -> assertThat(favorite.getTarget()).isEqualTo(구로디지털단지역)
        );
    }

    @DisplayName("즐겨찾기에 추가할 지하철역이 null 이라면 예외가 발생해야 한다")
    @Test
    void favorite_add_exception_test() {
        assertThatThrownBy(() -> {
            Favorite.of(대림역, null);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_FOUND_STATION.getMessage());

        assertThatThrownBy(() -> {
            Favorite.of(null, 대림역);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_FOUND_STATION.getMessage());
    }

    @DisplayName("즐겨찾기에 추가할 두 역이 같다면 예외가 발생해야 한다")
    @Test
    void favorite_add_exception_test2() {
        assertThatThrownBy(() -> {
            Favorite.of(대림역, 대림역);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_SAME_STATION.getMessage());
    }
}
