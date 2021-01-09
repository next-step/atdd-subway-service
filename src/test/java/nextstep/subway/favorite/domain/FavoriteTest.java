package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 기능")
class FavoriteTest {

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create() {
        final Member member = new Member("jaenyeong.dev@gmail.com", "1234", 31);
        final Station 출발지_강남역 = new Station("강남역");
        final Station 도착지_정자역 = new Station("정자역");

        final Favorite favorite = Favorite.of(member, 출발지_강남역, 도착지_정자역);

        assertThat(favorite).isNotNull();
    }

    @DisplayName("즐겨찾기 생성시 올바르지 않은 멤버 또는 역을 삽입한다.")
    @Test
    void createWhenGivenInvalidMemberAndInvalidStation() {
        final Member member = new Member("jaenyeong.dev@gmail.com", "1234", 31);
        final Station 출발지_강남역 = new Station("강남역");
        final Station 도착지_정자역 = new Station("정자역");

        assertAll(
            () -> assertThatThrownBy(() -> Favorite.of(null, 출발지_강남역, 도착지_정자역)).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> Favorite.of(member, null, 도착지_정자역)).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> Favorite.of(member, 출발지_강남역, null)).isInstanceOf(IllegalArgumentException.class)
        );
    }
}
