package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    public Favorite createFavorite(FavoriteRequest favoriteRequest) {
        return new Favorite();
    }
}
