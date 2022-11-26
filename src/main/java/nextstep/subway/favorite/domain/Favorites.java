package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {}

    private Favorites(List<Favorite> favorites) {
        this.favorites = new ArrayList<>(favorites);
    }

    public static Favorites from(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    public boolean isContainFavorite(Favorite checkFavorite) {
        return favorites.stream().anyMatch(favorite -> favorite.equals(checkFavorite));
    }
}
