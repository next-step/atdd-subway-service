package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.InvalidArgumentException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("즐겨찾기 도메인 테스트")
public class FavoriteTest {
    final Station 서울역 = new Station("서울역");
    final Station 용산역 = new Station("용산역");

    final Long 사용자_ID = 1L;
    final Long 신명진_ID = 2L;

    @Test
    @DisplayName("같은 즐겨찾기인지 확인")
    void equalsFavorite() {

        final Favorite favorite = Favorite.of(서울역, 용산역, 사용자_ID);

        assertAll(() -> {
            assertTrue(favorite.equalsFavorite(Favorite.of(서울역, 용산역, 사용자_ID)));
            assertFalse(favorite.equalsFavorite(Favorite.of(용산역, 서울역, 사용자_ID)));
            assertFalse(favorite.equalsFavorite(Favorite.of(서울역, 용산역, 신명진_ID)));
        });
    }

    @Test
    @DisplayName("다른사람의 즐겨찾기 삭제 시도 시 CannotDeleteException")
    void validateDelete() {

        assertThatThrownBy(() -> Favorite.of(서울역, 용산역, 사용자_ID).validateDelete(신명진_ID))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른사람의 즐겨찾기는 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 InvalidArgumentException")
    void validateEqualStation() {
        assertThatThrownBy(() -> Favorite.of(서울역, 서울역, 사용자_ID))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("출발역과 도착역이 같습니다.");
    }
}
