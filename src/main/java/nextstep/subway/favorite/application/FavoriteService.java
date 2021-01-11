package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final StationService stationService;

    public FavoriteService(StationService stationService,
            FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest request, long memberId) {
        Station sourceStation = stationService.findById(request.getSource());
        Station targetStation = stationService.findById(request.getTarget());

        Favorite favorite = request.toFavorite(memberId);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return FavoriteResponse.of(savedFavorite.getId(), sourceStation, targetStation);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorite(Long memberId) {
        Favorites favorites = Favorites.of(favoriteRepository.findByMemberId(memberId));
        Set<Long> stationIds = favorites.extractionIds();
        List<Station> stations = stationService.findByIds(stationIds);

        return favorites.stream()
                .map(it -> favoriteToResponse(stations, it))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    private FavoriteResponse favoriteToResponse(List<Station> stations, Favorite favorite) {
        return FavoriteResponse.of(
                favorite.getId(),
                findFirst(stations, favorite.getSource()),
                findFirst(stations, favorite.getTarget()));
    }

    private Station findFirst(List<Station> stations, Long id) {
        return stations.stream()
                .filter(it -> id.equals(it.getId()))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(id));
    }

}
