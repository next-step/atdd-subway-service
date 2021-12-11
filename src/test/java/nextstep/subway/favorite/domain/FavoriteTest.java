package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Favorite 테스트")
class FavoriteTest {
    @DisplayName("생성 테스트")
    @Test
    void 생성_테스트() {
        // given
        Long memberId = 1L;
        Station source = new Station("강남역");
        Station target = new Station("역삼역");

        // when
        Favorite favorite = Favorite.of(memberId, source, target);

        // then
        assertAll(
                () -> assertThat(favorite).isNotNull(),
                () -> assertThat(favorite.getMemberId()).isEqualTo(memberId),
                () -> assertThat(favorite.getSource()).isEqualTo(source),
                () -> assertThat(favorite.getTarget()).isEqualTo(target)
        );
    }

    @DisplayName("즐겨찾기의 출발역, 도착역, 회원 id 는 필수이다.")
    @Nested
    class CreateFailTest {
        @DisplayName("잘못된 memberId")
        @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
        @CsvSource(value = {"-1", "0", "null"}, nullValues = {"null"})
        void 잘못된_memberId(Long memberId) {
            // given
            Station source = new Station("강남역");
            Station target = new Station("역삼역");

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> Favorite.of(memberId, source, target);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(FavoriteException.class);
        }

        @DisplayName("Source Station이 비어있음")
        @Test
        void Source_Station이_비어있음() {
            // given
            Long memberId = 1L;
            Station source = null;
            Station target = new Station("역삼역");

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> Favorite.of(memberId, source, target);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(FavoriteException.class);
        }

        @DisplayName("Target Station이 비어있음")
        @Test
        void Target_Station이_비어있음() {
            // given
            Long memberId = 1L;
            Station source = new Station("강남역");
            Station target = null;

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> Favorite.of(memberId, source, target);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(FavoriteException.class);
        }
    }
}