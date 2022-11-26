package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.constant.ErrorCode;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {}

    private Favorites(List<Favorite> favorites) {
        this.favorites = new ArrayList<>(favorites);
    }

    public static Favorites from(List<Favorite> favorites) {
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
            throw new IllegalArgumentException(ErrorCode.이미_존재하는_즐겨찾기.getErrorMessage());
        }
    }

    private void validateMember(Favorite favorite) {
        if(isNotEqualMember(favorite)) {
            throw new IllegalArgumentException(ErrorCode.즐겨찾기들의_회원은_동일해야_함.getErrorMessage());
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
