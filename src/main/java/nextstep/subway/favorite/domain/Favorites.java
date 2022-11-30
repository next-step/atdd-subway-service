package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {}

    private Favorites(List<Favorite> favorites) {
        this.favorites = new ArrayList<>(favorites);
    }

    public static Favorites from(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    public void addFavorite(Favorite favorite) {
        if (isNotContain(favorite)){
            favorites.add(favorite);
        }
    }

    private boolean isNotContain(Favorite favorite) {
        return !favorites.contains(favorite);
    }

    public List<Favorite> list() {
        return favorites;
    }
}
