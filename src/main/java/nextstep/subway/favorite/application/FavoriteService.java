package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        return null;
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return null;
    }

    public void deleteFavorite(Long id) {
    }
}
