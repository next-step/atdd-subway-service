package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest) {
        return null;
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        return null;
    }

    public void deleteFavorite(Long id, Long id1) {

    }
}
