package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Favorite;
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
 * fileName : FavoriteTest
 * author : haedoang
 * date : 2021/12/06
 * description :
 */
public class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기를 생성한다.")
    public void create() throws Exception {
        //given
        Favorite favorite = new Favorite(
                new Station("강남역"),
                new Station("역삼역"),
                Distance.of(100)
        );
        //when
        Member member = new Member("haedoang@gmail.com", "11", 33);
        member.addFavorite(favorite);

        //then
        assertThat(member.getFavorites().getList()).hasSize(1);
        assertThat(favorite.getDistance()).isEqualTo(Distance.of(100));
    }

    @Test
    @DisplayName("즐겨찾기 중복 체크")
    public void duplicate() {
        //given
        Favorite favorite = new Favorite(
                new Station("강남역"),
                new Station("역삼역"),
                Distance.of(100)
        );

        //when
        Member member = new Member("haedoang@gmail.com", "11", 33);
        member.addFavorite(favorite);

        assertThatThrownBy(() -> member.addFavorite(favorite))
                .isInstanceOf(FavoriteDuplicatedException.class)
                .hasMessageContaining(FavoriteDuplicatedException.message);
    }
}
