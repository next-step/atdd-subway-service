package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {

    public FavoriteResponse createFavorite(FavoriteRequest request) {
        return null;
    }

    public void deleteFavorite(Long id) {

    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll() {
        return null;
    }
}
