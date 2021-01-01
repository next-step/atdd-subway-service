package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {
    @DisplayName("인자가 하나라도 없으면 오브젝트를 생성할 수 없다.")
    @Test
    void createFailTest() {
        assertThatThrownBy(() -> new Favorite(null, null, null))
                .isInstanceOf(FavoriteCreationException.class);
    }
}
