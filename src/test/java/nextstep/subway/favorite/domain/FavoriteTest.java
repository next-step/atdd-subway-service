package nextstep.subway.favorite.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteTest {
    private static final long MEMBER_ID = 1L;
    private static final long OTHER_MEMBER_ID = 2L;
    private static final long 강남역_ID = 1L;
    private static final long 광교역_ID = 2L;

    @DisplayName("즐겨찾기 생성한 사람이면 참, 생성한 사람이 아니면 거짓 반환")
    @Test
    void isCreatedBy() {
        Favorite favorite = new Favorite(MEMBER_ID, 강남역_ID, 광교역_ID);

        assertThat(favorite.isCreatedBy(MEMBER_ID)).isTrue();
        assertThat(favorite.isCreatedBy(OTHER_MEMBER_ID)).isFalse();
    }
}
