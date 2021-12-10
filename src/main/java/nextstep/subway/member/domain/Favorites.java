package nextstep.subway.member.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.member.application.exception.FavoriteErrorCode;

@Embeddable
public class Favorites {


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "member_id", updatable = false, nullable = false)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {

    }

    public static Favorites of() {
        return new Favorites();
    }

    public void add(Favorite favorite) {
        validateDuplicate(favorite);
        this.favorites.add(favorite);
    }

    public void remove(Favorite favorite) {
        this.favorites.remove(favorite);
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    private void validateDuplicate(Favorite favorite) {
        if (isDuplicate(favorite)) {
            throw InvalidParameterException.of(FavoriteErrorCode.NOT_DUPLICATE);
        }
    }

    private boolean isDuplicate(Favorite favorite) {
        return favorites.stream()
            .anyMatch(favorite::isDuplicate);
    }

    public void removeById(Long favoriteId) {
        favorites.stream()
            .filter(favorite -> favorite.isSameId(favoriteId))
            .findFirst()
            .ifPresent(this::remove);
    }

    public List<Long> getFavoriteStationIds() {
        return favorites.stream()
            .map(Favorite::getStationIds)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
