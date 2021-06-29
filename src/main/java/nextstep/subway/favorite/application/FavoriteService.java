package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        return new FavoriteResponse();
    }

    public List<FavoriteResponse> findFavorites(Long id) {

        return new ArrayList<>();
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {

    }
}
