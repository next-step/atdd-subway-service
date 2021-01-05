package nextstep.subway.favorites.service;

import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoritesService {

    public Favorite createFavorite(FavoritesRequest favoriteRequest) {

        return new Favorite();
    }

    public FavoritesResponse findAll() {
        return new FavoritesResponse();
    }

    public void delete(Long id) {

    }
}
