package nextstep.subway.favorite.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member")
    private final List<Favorite> favorites = new ArrayList<>();

    public boolean isContain(Favorite favorite) {
        return this.favorites.contains(favorite);
    }

    public void addFavorite(final Favorite favorite) {
        if (!this.favorites.contains(favorite)) {
            this.favorites.add(favorite);
        }
    }

    public List<Favorite> getFavorites() {
        return favorites.stream().filter(favorite -> !favorite.isDeleted()).collect(Collectors.toList());
    }
}
