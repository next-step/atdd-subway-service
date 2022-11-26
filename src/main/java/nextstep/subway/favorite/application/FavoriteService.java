package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return null;
    }

    public void createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
    }


    public void removeFavorite(LoginMember loginMember, Long favoriteId) {
    }
}
