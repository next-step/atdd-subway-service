package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
        return null;
    }

    public List<FavoriteResponse> findFavorites() {
        return null;
    }

    public void deleteFavoriteById(Long id) {
    }
}
