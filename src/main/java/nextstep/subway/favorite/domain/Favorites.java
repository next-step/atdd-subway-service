package nextstep.subway.favorite.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Favorites {
    private List<Favorite> favorites;

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public static Favorites of(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    public List<Long> findStationByIds() {
        return favorites.stream()
                .flatMap(favorite -> Stream.of(favorite.getSource(), favorite.getTarget()))
                .collect(Collectors.toList());
    }

    public List<Favorite> get() {
        return favorites;
    }
}