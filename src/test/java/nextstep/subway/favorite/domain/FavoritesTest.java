package nextstep.subway.favorite.domain;

import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.exception.FavoriteDuplicatedException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : nextstep.subway.favorite.domain
 * fileName : FavoritesTest
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
public class FavoritesTest {
    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 잠실역 = new Station("잠실역");
    private final Distance DISTANCE_5 = Distance.of(5);
    private final Member 사용자 = new Member("haedoang@gmail.com", "11", 33);

    public static Favorite of(Station sourceStation, Station targetStation, Distance distance) {
        return new Favorite(sourceStation, targetStation, distance);
    }

    @DisplayName("Favorites 생성하기")
    @Test
    public void create() {
        // give
        Favorite favorite = Favorite.of(강남역, 역삼역, DISTANCE_5);

        // when
        사용자.addFavorite(favorite);

        // then
        assertThat(사용자.getFavorites().size()).isEqualTo(1);
        assertThat(사용자.getFavorites().findFavorite(favorite)).isNotNull();
    }

    @DisplayName("Favorites 중복 체크하기")
    @Test
    public void validateFavorites() {
        // give
        Favorite favorite = Favorite.of(강남역, 역삼역, DISTANCE_5);

        // when
        사용자.addFavorite(favorite);

        // then
        assertThatThrownBy(() -> 사용자.addFavorite(favorite)).isInstanceOf(FavoriteDuplicatedException.class)
                .hasMessageContaining(FavoriteDuplicatedException.message);
    }

}
