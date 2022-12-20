package nextstep.subway.favorite.application;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteCreateRequest request) {
        return new FavoriteResponse();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return new ArrayList<>();
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        //
    }
}
