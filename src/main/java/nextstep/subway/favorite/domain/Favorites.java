package nextstep.subway.favorite.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Favorites {

    private final List<Favorite> list;

    private Favorites(List<Favorite> list) {
        Assert.notNull(list, "즐겨찾기 목록은 필수입니다.");
        this.list = list;
    }

    public static Favorites from(List<Favorite> list) {
        return new Favorites(list);
    }

    public <R> List<R> mapToList(Function<Favorite, R> mapper) {
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
}
