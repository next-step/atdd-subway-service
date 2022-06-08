package nextstep.subway.favorite.domain;

import static nextstep.subway.favorite.enums.FavoriteExceptionType.FAVORITE_FILED_IS_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.stream.Stream;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FavoriteTest {

    @DisplayName("출발역, 도착역, 회원 정보를 바탕으로 즐겨찾기 도메인을 생성할 수 있다.")
    @Test
    void createFavorite() {
        // given
        Station 수서역 = Station.from("수서역");
        Station 판교역 = Station.from("판교역");
        Member member = new Member();

        // when
        Favorite favorite = Favorite.of(수서역, 판교역, member);

        // then
        assertThat(favorite).isNotNull();
    }

    @DisplayName("출발역, 도착역, 회원 정보 중 하나라도 Null인 경우 즐겨찾기 도메인을 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource("favoriteFieldProvider")
    void exceptionCreateFavorite(Station source, Station target, Member member) {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Favorite.of(source, target, member))
                .withMessageContaining(FAVORITE_FILED_IS_NOT_NULL.getMessage());
    }

    private static Stream<Arguments> favoriteFieldProvider() {
        return Stream.of(
                Arguments.of(null, Station.from("판교역"), new Member()),
                Arguments.of(Station.from("수서역"), null, new Member()),
                Arguments.of(Station.from("수서역"), Station.from("판교역"), null)
        );
    }
}