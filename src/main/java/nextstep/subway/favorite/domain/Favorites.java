package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.exception.InvalidParameterException;

@Embeddable
public class Favorites {
    private static final String ERROR_MESSAGE_IS_NOT_CONTAIN_FAVORITE = "즐겨찾기를 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {}

    private Favorites(List<Favorite> favorites) {
        this.favorites = new ArrayList<>(favorites);
    }

    public static Favorites from(List<Favorite> favorites) {
        return new Favorites(favorites);
    }

    public void addFavorite(Favorite favorite) {
        if (isNotContain(favorite)){
            favorites.add(favorite);
        }
    }

    public void removeFavorite(Favorite favorite) {
        if (isNotContain(favorite)) {
            throw new InvalidParameterException(ERROR_MESSAGE_IS_NOT_CONTAIN_FAVORITE);
        }
        favorites.remove(favorite);
    }

    private boolean isNotContain(Favorite favorite) {
        return !favorites.contains(favorite);
    }

    public List<Favorite> list() {
        return favorites;
    }
}
