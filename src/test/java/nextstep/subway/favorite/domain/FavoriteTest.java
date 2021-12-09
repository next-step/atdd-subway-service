package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class FavoriteTest {
    final Station 서울역 = new Station("서울역");
    final Station 용산역 = new Station("용산역");
    final Station 남영역 = new Station("남영역");

    final Member 사용자 = new Member("email@email.com", "password", 20);
    final Member 신명진 = new Member("shinmj@email.com", "password", 20);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(사용자, "id", 1L);
        ReflectionTestUtils.setField(신명진, "id", 2L);
    }

    @Test
    @DisplayName("같은 즐겨찾기인지 확인")
    void equalsFavorite() {

        final Favorite favorite = Favorite.of(서울역, 용산역, 사용자);

        assertAll(() -> {
            assertTrue(favorite.equalsFavorite(Favorite.of(서울역, 용산역, 사용자)));
            assertFalse(favorite.equalsFavorite(Favorite.of(용산역, 서울역, 사용자)));
            assertFalse(favorite.equalsFavorite(Favorite.of(서울역, 용산역, 신명진)));
        });
    }

    @Test
    @DisplayName("다른사람의 즐겨찾기 삭제 시도 시 CannotDeleteException")
    void validateDelete() {

        assertThatThrownBy(() -> Favorite.of(서울역, 용산역, 사용자).validateDelete(신명진))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른사람의 즐겨찾기는 삭제할 수 없습니다.");
    }
}
