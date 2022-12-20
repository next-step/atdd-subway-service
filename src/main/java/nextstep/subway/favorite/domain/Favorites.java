package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();
    public void add(Favorite favorite) {
        validateDuplicate(favorite);
        this.favorites.add(favorite);
    }

    private void validateDuplicate(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new IllegalArgumentException("이미 즐겨찾기에 등록한 구간입니다");
        }
    }
}
