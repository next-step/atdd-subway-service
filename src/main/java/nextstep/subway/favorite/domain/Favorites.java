package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();


    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
