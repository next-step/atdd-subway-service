package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.domain.Favorite;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class FavoriteGroup {
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected FavoriteGroup() {
    }

    public FavoriteGroup(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public boolean contains(Favorite favorite) {
        return favorites.contains(favorite);
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        favorites.remove(favorite);
    }

    public Optional<Favorite> findFavoriteById(Long favoriteId) {
        return favorites.stream()
                .filter(favorite -> favorite.getId().equals(favoriteId))
                .findFirst();
    }
}
