package nextstep.subway.favorite.infrastructure;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

public class InMemoryFavoriteRepository implements FavoriteRepository {
    private final Map<Long, Favorite> elements = new HashMap<>();
    private long favoriteId = 0L;

    public InMemoryFavoriteRepository() {

    }

    @Override
    public Favorite save(Favorite favorite) {
        ReflectionTestUtils.setField(favorite, "id", ++favoriteId);
        elements.put(favoriteId, favorite);
        return favorite;
    }
}
