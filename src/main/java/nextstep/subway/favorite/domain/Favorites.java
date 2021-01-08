package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    final List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {
    }

    public static Favorites createInstance() {
        return new Favorites();
    }
}
