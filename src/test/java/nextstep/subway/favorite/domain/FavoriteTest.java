package nextstep.subway.favorite.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {

    @Test
    void create() {
        long sourceStationId = 1L;
        long targetStationId = 1L;
        long loginMemberId = 1L;
        Favorite favorite = new Favorite(loginMemberId, sourceStationId, targetStationId);

        assertThat(favorite).isNotNull();
    }
}
