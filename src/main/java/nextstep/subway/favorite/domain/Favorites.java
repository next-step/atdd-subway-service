package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.member.domain.Member;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public Favorites() {

    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }

    public void addFavorite(Favorite favorite) {
        checkRedundantFavorite(favorite);
        favorites.add(favorite);
    }

    public void deleteFavorite(Favorite favorite, Member member) {
        checkOwner(favorite, member);
        favorites.remove(favorite);
    }

    private void checkOwner(Favorite favorite, Member member) {
        if (favorite.isSameOwner(member)) {
            throw new IllegalArgumentException("본인의 즐겨찾기만 삭제할 수 있습니다.");
        }
    }

    private void checkRedundantFavorite(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new IllegalArgumentException("이미 등록된 즐겨찾기 입니다.");
        }
    }
}
