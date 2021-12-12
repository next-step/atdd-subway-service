package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoritesTest {

    private Favorites favorites;
    private Favorite favorite;


    @BeforeEach
    public void setUp() {
        // given
        favorites = Favorites.of();
        favorite = Favorite.of(1L, 2L);
    }

    @Test
    void Favorite_추가() {
        // when
        favorites.add(favorite);

        // then
        assertThat(favorites.getFavorites()).hasSize(1);
    }

    @Test
    void Favorite_제거() {
        // when
        Favorite remove = Favorite.of(2L, 3L);
        favorites.add(favorite);
        favorites.add(remove);
        // then
        assertThat(favorites.getFavorites()).hasSize(2);

        // when
        favorites.remove(remove);
        // then
        assertThat(favorites.getFavorites()).hasSize(1);
    }

    @Test
    void Favorite_중복추가_실패() {
        // when
        favorites.add(favorite);

        // then
        assertThrows(InvalidParameterException.class, () -> favorites.add(favorite));
    }
}
