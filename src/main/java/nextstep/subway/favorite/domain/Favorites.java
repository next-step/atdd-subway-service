package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }
}
