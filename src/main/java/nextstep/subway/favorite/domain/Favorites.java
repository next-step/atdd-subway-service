package nextstep.subway.favorite.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.favorite.dto.FavoritePair;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public List<FavoritePair> getFavoritePairs() {
        return favorites.stream()
            .map(FavoritePair::of)
            .collect(toList());
    }
}
