package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favorites {
    private List<Favorite> values = new ArrayList<>();

    public Favorites(List<Favorite> values) {
        this.values = values;
    }

    public List<Favorite> get() {
        return Collections.unmodifiableList(values);
    }
}
