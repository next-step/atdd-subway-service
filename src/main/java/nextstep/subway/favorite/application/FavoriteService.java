package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    public FavoriteResponse saveFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        return new FavoriteResponse(1L,null, null);
    }
}
