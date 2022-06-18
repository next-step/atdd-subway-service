package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    public FavoriteResponse findFavorites(Long id) {
        return null;
    }

    public void updateFavorites(Long id, FavoriteRequest param) {

    }

    public void deleteFavorite(Long id) {

    }
}
