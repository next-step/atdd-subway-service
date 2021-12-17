package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoriteRequest;
import nextstep.subway.favorite.domain.FavoriteResponse;

import java.util.List;

public class FavoriteService {
    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        return null;
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        return null;
    }

    public FavoriteResponse removeFavorite(LoginMember loginMember, Long favoriteId) {
        return null;
    }
}
