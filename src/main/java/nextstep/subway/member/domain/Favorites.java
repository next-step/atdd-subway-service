package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member")
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public boolean isContainsFavorite(Favorite favorite) {
        return favorites.contains(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }
}
