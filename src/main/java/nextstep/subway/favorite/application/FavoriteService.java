package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository,
        FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse addFavorite(LoginMember member, FavoriteRequest request) {
        Station sourceStation = readStationById(request.getSource());
        Station targetStation = readStationById(request.getTarget());

        Favorite favorite = favoriteRepository.save(
            Favorite.of(member, sourceStation, targetStation));

        return FavoriteResponse.of(favorite);
    }

    public Station readStationById(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 역이 없습니다. id=" + stationId));
    }
}
