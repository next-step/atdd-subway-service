package nextstep.subway.favorite.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteRequestTest {

    @Test
    void 출발역과_도착역이_같을경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() ->
                new FavoriteRequest(1L, 1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
