package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FavoriteService {

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        // TODO
        return new FavoriteResponse();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        // TODO
        return new ArrayList<>();
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        // TODO
    }
}
