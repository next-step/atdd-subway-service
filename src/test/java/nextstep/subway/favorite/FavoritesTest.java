package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.Favorites;

public class FavoritesTest {

    private Favorites favorites;
    private Favorite favorite;

    @BeforeEach
    public void setUp() {
        this.favorites = new Favorites();
        this.favorite = new Favorite(null, null, null);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void add() {
        favorites.add(favorite);

        assertThat(favorites.getFavoritePairs()).hasSize(1);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void remove() {
        favorites.add(favorite);
        favorites.delete(favorite);

        assertThat(favorites.getFavoritePairs()).hasSize(0);
    }
}
