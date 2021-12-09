package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;

@Embeddable
public class Favorites {

    private List<Favorite> favorites = new ArrayList<>();

    public static Favorites of(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    protected Favorites() {
    }

    private Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Favorite> favoriteOfMine() {
        return Collections.unmodifiableList(favorites);
    }
}
