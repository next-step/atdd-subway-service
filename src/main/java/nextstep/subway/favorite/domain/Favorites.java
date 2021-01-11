package nextstep.subway.favorite.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-10
 */
public class Favorites {

    private final List<Favorite> favorites;

    private Favorites(List<Favorite> favorites) {
        if (favorites == null) {
            throw new IllegalArgumentException();
        }
        this.favorites = favorites;
    }

    public static Favorites of(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    public Stream<Favorite> stream() {
        return favorites.stream();
    }

    public Set<Long> extractionIds() {
        Set<Long> stationIds = new HashSet<>();
        this.favorites.forEach(it -> {
            stationIds.add(it.getSource());
            stationIds.add(it.getTarget());
        });
        return stationIds;
    }


}
