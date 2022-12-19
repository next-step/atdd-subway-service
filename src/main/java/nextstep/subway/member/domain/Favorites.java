package nextstep.subway.member.domain;

import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.favorite.domain.Favorite;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites(){}

    private Favorites(List<Favorite> favorites){
        this.favorites = new ArrayList<>(favorites);
    }
    private static Favorites from(List<Favorite> favorites){
        return new Favorites(favorites);
    }
    public boolean isContainFavorite(Favorite checkFavorite) {
        return favorites.stream().anyMatch(favorite -> favorite.equals(checkFavorite));
    }

    public void addFavorite(Favorite favorite) {
        validateDuplicateFavorite(favorite);
        validateMember(favorite);
        this.favorites.add(favorite);
    }

    private void validateDuplicateFavorite(Favorite favorite) {
        if(isContainFavorite(favorite)) {
            throw new IllegalArgumentException(ErrorMessage.EXIST_FAVORITE.getMessage());
        }
    }

    public void deleteFavorite(Favorite deleteFavorite) {
        favorites.removeIf(favorite -> favorite.equals(deleteFavorite));
    }

    private void validateMember(Favorite favorite) {
        if(isNotEqualMember(favorite)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EQUALS_MEMBER.getMessage());
        }
    }

    private boolean isNotEqualMember(Favorite favorite) {
        if(favorites.isEmpty()) {
            return false;
        }
        return !favorites.get(0).hasSameMember(favorite);
    }

    public List<Favorite> findFavorites() {
        return Collections.unmodifiableList(favorites);
    }
}
