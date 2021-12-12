package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class FavoriteService {

    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorites(FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        return favoriteRepository.save(Favorite.of(source, target));
    }

    public List<FavoriteResponse> findAll() {
        return Collections.emptyList();
    }

    public void deleteFavorites(Long id) {

    }
}
