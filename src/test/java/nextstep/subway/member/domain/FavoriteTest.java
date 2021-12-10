package nextstep.subway.member.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    @Test
    @DisplayName("stationSourceId null 이면 에러")
    void create_stationSourceId_null_fail() {
        // then
        assertThrows(InvalidParameterException.class, () -> new Favorite(null, 2L));
    }

    @Test
    @DisplayName("stationTargetId null 이면 에러")
    void create__stationTarget_null_fail() {
        // then
        assertThrows(InvalidParameterException.class, () -> new Favorite(1L, null));
    }

    @Test
    @DisplayName("stationSourceId stationTargetId 같으면 에러")
    void create_id_same_fail() {
        // then
        assertThrows(InvalidParameterException.class, () -> new Favorite(1L, 1L));
    }
}
