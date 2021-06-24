package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class FavoriteGroup {
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public FavoriteGroup() {
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
}
