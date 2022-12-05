package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;

public class Favorites {

    public static final String FAVORITE_DUPLICATE_EXCEPTION_MESSAGE = "출발역과 도착역이 같은 즐겨찾기를 생성할 수 없다.";
    private List<Favorite> favorites = new ArrayList<>();

    private static void validateDuplicate(Favorite favorite, Favorite value) {
        if (value.equals(favorite)) {
            throw new IllegalArgumentException(FAVORITE_DUPLICATE_EXCEPTION_MESSAGE);
        }
    }

    public void add(Favorite favorite) {
        validate(favorite);
        this.favorites.add(favorite);
    }

    private void validate(Favorite favorite) {
        for (Favorite value : this.favorites) {
            validateDuplicate(favorite, value);
        }
    }
}
