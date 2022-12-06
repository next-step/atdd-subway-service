package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Favorites {

    public static final String FAVORITE_DUPLICATE_EXCEPTION_MESSAGE = "출발역과 도착역이 같은 즐겨찾기를 생성할 수 없다.";
    public static final String HAS_NOT_FAVORITE_EXCEPTION_MESSAGE = "회원님은 삭제하려는 즐겨찾기를 가지고 있지 않습니다.";
    private List<Favorite> favorites = new ArrayList<>();

    public Favorites() {
    }

    public void addAll(List<Favorite> favorites) {
        this.favorites.addAll(favorites);
    }

    public void add(Favorite favorite) {
        validate(favorite);
        this.favorites.add(favorite);
    }

    public void validateDeleteFavorite(Favorite deleteFavorite) {
        if (!this.favorites.contains(deleteFavorite)) {
            throw new NoSuchElementException(HAS_NOT_FAVORITE_EXCEPTION_MESSAGE);
        }
    }

    private void validate(Favorite favorite) {
        for (Favorite value : this.favorites) {
            validateDuplicate(favorite, value);
        }
    }

    private static void validateDuplicate(Favorite favorite, Favorite value) {
        if (value.isSame(favorite)) {
            throw new IllegalArgumentException(FAVORITE_DUPLICATE_EXCEPTION_MESSAGE);
        }
    }
}
