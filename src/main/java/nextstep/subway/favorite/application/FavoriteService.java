package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoritesResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    public Favorite saveFavoriteOfMine(Long memberId, FavoriteRequest favoriteRequest) {
        return new Favorite();
    }

    public FavoritesResponse findFavoriteOfMine(Long memberId) {
        return new FavoritesResponse();
    }

    public void deleteFavoriteOfMine(Long memberId, Long favoriteId) {
    }
}
