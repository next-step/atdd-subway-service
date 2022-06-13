package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoritesResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    public FavoriteResponse saveFavoriteOfMine(Long memberId, FavoriteRequest favoriteRequest) {
        return new FavoriteResponse();
    }

    public FavoritesResponse findFavoriteOfMine(Long memberId) {
        return new FavoritesResponse();
    }

    public void deleteFavoriteOfMine(Long memberId, Long favoriteId) {
    }
}
