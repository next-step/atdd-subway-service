package nextstep.subway.favorites.domain;

import nextstep.subway.member.exception.FavoriteDuplicatedException;
import nextstep.subway.member.exception.FavoriteNotFoundException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName : nextstep.subway.favorites.domain
 * fileName : Favorites
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    private void validateDuplicate(Favorite favorite) {
        if (isExist(favorite)) {
            throw new FavoriteDuplicatedException();
        }
    }

    public boolean isExist(Favorite target) {
        if (!favorites.isEmpty()) {
            return favorites.stream()
                    .allMatch(favorite -> favorite.isDuplicate(target));
        }
        return false;
    }

    public void add(Favorite favorite) {
        validateDuplicate(favorite);
        favorites.add(favorite);
    }

    public void remove(Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);
        favorites.remove(favorite);
    }

    public Favorite findFavorite(Long id) {
        return favorites.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(FavoriteNotFoundException::new);
    }

    public Favorite findFavorite(Favorite favorite) {
        return favorites.stream()
                .filter(it -> it.equals(favorite))
                .findFirst()
                .orElseThrow(FavoriteNotFoundException::new);
    }

    public List<Favorite> getList() {
        return favorites;
    }

    public int size() {
        return favorites.size();
    }
}
