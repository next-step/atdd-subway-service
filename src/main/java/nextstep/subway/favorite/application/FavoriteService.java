package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    public FavoriteResponse createFavorite(Long id, FavoriteRequest favoriteRequest) {
        return new FavoriteResponse(1L, 1L, 2L, 1L);
    }
}
