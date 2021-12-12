package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
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

        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(toList());
    }

    public void deleteFavorites(Long id) {
        favoriteRepository.deleteById(id);
    }
}
