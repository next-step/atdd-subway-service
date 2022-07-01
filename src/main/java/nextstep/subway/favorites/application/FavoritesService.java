package nextstep.subway.favorites.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;

    public FavoritesService(
        FavoritesRepository favoritesRepository,
        StationService stationService
    ) {
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoritesResponse saveFavorites(FavoritesRequest favoritesRequest) {
        Station sourceStation = stationService.findStationById(favoritesRequest.getSourceId());
        Station targetStation = stationService.findStationById(favoritesRequest.getTargetId());
        Favorites persistFavorites = favoritesRepository.save(favoritesRequest.toEntity(sourceStation, targetStation));
        return FavoritesResponse.from(persistFavorites);
    }

    public List<FavoritesResponse> findAllFavorites() {
        List<Favorites> persistFavorites = favoritesRepository.findAll();
        return persistFavorites.stream()
            .map(FavoritesResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoritesById(Long id) {
        favoritesRepository.deleteById(id);
    }
}
