package nextstep.subway.favorite;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class FavoriteTest {

    private final Member member = new Member("abc@abc.co.kr", "1234", 20);
    private Favorite favorite1;

    @BeforeEach
    void setUp() {
        Station source1 = mock(Station.class);
        Station target1 = mock(Station.class);
        favorite1 = new Favorite(member, source1, target1);

        Station source2 = mock(Station.class);
        Station target2 = mock(Station.class);
        Favorite favorite2 = new Favorite(member, source2, target2);

        member.addFavorite(favorite1);
        member.addFavorite(favorite2);
    }

    @Test
    @DisplayName("사용자의 즐겨찾기를 조회한다.")
    void getFavorites() {
        assertThat(member.getFavorites()).hasSize(2);
    }

    @Test
    @DisplayName("즐겨찾기를 추가한다.")
    void addFavorite() {
        assertThat(member.getFavorites()).hasSize(2);
        Station sourceAdd = mock(Station.class);
        Station targetAdd = mock(Station.class);
        Favorite favoriteAdd = new Favorite(member, sourceAdd, targetAdd);
        member.addFavorite(favoriteAdd);
        assertThat(member.getFavorites()).hasSize(3);
    }

    @Test
    @DisplayName("나의 즐겨찾기를 삭제한다.")
    void removeFavoriteOfMine() {
        assertThat(member.getFavorites()).hasSize(2);
        member.removeFavorite(favorite1);
        assertThat(member.getFavorites()).hasSize(1);
    }

    @Test
    @DisplayName("다른 사용자의 즐겨찾기를 삭제한다.")
    void removeFavoriteOfNotMine() {
        Member memberOfNotMine = new Member("efg@abc.co.kr", "1234", 20);
        Station sourceOfNotMine = mock(Station.class);
        Station targetOfNotMine = mock(Station.class);
        Favorite favoriteOfNotMine = new Favorite(memberOfNotMine, sourceOfNotMine, targetOfNotMine);
        memberOfNotMine.addFavorite(favoriteOfNotMine);

        assertThatThrownBy(() -> member.removeFavorite(favoriteOfNotMine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("즐겨찾기에 대한 권한이 없습니다.");
    }
}
