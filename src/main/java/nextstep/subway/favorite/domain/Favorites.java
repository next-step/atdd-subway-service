package nextstep.subway.favorite.domain;

import nextstep.subway.enums.ErrorMessage;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Favorite> favorites = new ArrayList<>();

    public void add(Favorite favorite) {
        validateDuplicate(favorite);
        this.favorites.add(favorite);
    }

    private void validateDuplicate(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_FAVORITE.getMessage());
        }
    }

    public List<Favorite> values() {
        return Collections.unmodifiableList(favorites);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorites favorites1 = (Favorites) o;
        return Objects.equals(favorites, favorites1.favorites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(favorites);
    }
}
