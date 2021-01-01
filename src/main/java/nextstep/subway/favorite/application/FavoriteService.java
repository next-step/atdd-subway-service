package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.adapters.SafeStationAdapter;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SafeStationAdapter safeStationAdapter;

    public FavoriteService(FavoriteRepository favoriteRepository, SafeStationAdapter safeStationAdapter) {
        this.favoriteRepository = favoriteRepository;
        this.safeStationAdapter = safeStationAdapter;
    }

    public Long saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        validateStationsExists(favoriteRequest);

        Favorite saved = favoriteRepository.save(
                new Favorite(loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget()));

        return saved.getId();
    }

    private void validateStationsExists(final FavoriteRequest favoriteRequest) {
        if(!safeStationAdapter.isAllExists(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
            throw new FavoriteCreationException("존재하지 않는 역으로 즐겨찾기를 생성할 수 없습니다.");
        }
    }
}
