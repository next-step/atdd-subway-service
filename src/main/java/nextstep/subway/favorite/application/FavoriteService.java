package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        return favoriteRepository.save(Favorite.of(loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget()));
    }
}
