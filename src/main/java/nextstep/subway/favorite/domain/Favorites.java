package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<nextstep.subway.favorite.domain.Favorite> favorites = new ArrayList<>();

    public void addFavorite(nextstep.subway.favorite.domain.Favorite favorite) {
        favorites.add(favorite);
    }

    public List<nextstep.subway.favorite.domain.Favorite> getFavorites() {
        return favorites;
    }

    public void removeFavorite(nextstep.subway.favorite.domain.Favorite favorite) {
        favorites.remove(favorite);
    }
}
