package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public FavoriteResponse findFavoriteResponseById(Long id) {
        Favorite persistFavorite = findFavoriteById(id);
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findAllFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse save(FavoriteRequest favoriteRequest) {
        Station sourceStation = stationRepository.findById(favoriteRequest.getSource())
                .orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(favoriteRequest.getTarget())
                .orElseThrow(NoSuchElementException::new);
        Favorite persistFavorite = favoriteRepository.save(new Favorite(sourceStation, targetStation));
        return FavoriteResponse.of(persistFavorite);
    }

    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }

}
