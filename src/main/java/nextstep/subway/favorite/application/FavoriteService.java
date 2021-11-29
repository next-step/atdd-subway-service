package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse create(FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(Long.valueOf(favoriteRequest.getSource()));
        Station targetStation = stationService.findById(Long.valueOf(favoriteRequest.getTarget()));
        
        Favorite createdFavorite = favoriteRepository.save(Favorite.of(sourceStation, targetStation));
        
        return FavoriteResponse.of(createdFavorite);
    }

    public List<FavoriteResponse> findAll() {
        return favoriteRepository.findAll().stream()
                                    .map(FavoriteResponse::of)
                                    .collect(Collectors.toList());
    }

    public void deleteById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

}
