package nextstep.subway.favorite.domain;

import static nextstep.subway.station.step.StationStep.station;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기")
class FavoritesTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() ->
                Favorites.from(Collections.singletonList(
                    Favorite.from(station("광교"), station("양재"), 1)))
            );
    }

    @Test
    @DisplayName("초기 목록은 필수")
    void instance_nullList_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Favorites.from(null))
            .withMessage("즐겨찾기 목록은 필수입니다.");
    }
}
