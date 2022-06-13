package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService,
                           FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse save(FavoriteRequest request) {
        Station sourceStation = stationService.findStationById(request.getSourceStationId());
        Station targetStation = stationService.findStationById(request.getTargetStationId());
        Favorite favorite = new Favorite(sourceStation, targetStation);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return new FavoriteResponse(savedFavorite.getId(),
                StationResponse.of(savedFavorite.getSourceStation()),
                StationResponse.of(savedFavorite.getTargetStation())
        );
    }
}
