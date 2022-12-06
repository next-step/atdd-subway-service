package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;

@Embeddable
public class Favorites {
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {}

    private Favorites(List<Favorite> favorites) {
        this.favorites = new ArrayList<>(favorites);
    }

    public static Favorites from(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    public List<Favorite> list() {
        return favorites;
    }
}
