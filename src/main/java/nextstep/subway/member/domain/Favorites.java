package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Favorites {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Fetch(FetchMode.JOIN)
    private List<Favorite> favorites = new ArrayList<>();

    protected Favorites() {
    }

    public List<Favorite> values() {
        return Collections.unmodifiableList(favorites);
    }

    public void add(Favorite favorite) {
        favorites.add(favorite);
    }

    public boolean isExist(Favorite favorite) {
        return favorites.contains(favorite);
    }
}
