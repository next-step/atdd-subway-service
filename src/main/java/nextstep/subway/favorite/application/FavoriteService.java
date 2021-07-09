package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Station source = stationService.findStation(request.getSource());
        Station target = stationService.findStation(request.getTarget());
        Favorite favorite = request.toFavorite(memberId);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(savedFavorite.getId(), source, target);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Favorites favorites = Favorites.of(favoriteRepository.findByMemberId(memberId));
        List<Long> stationIds = favorites.findStationByIds();
        List<Station> stations = stationService.findAllByIds(stationIds);
        return ofFavoriteResponse(favorites, stations);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private List<FavoriteResponse> ofFavoriteResponse(Favorites favorites, List<Station> stations) {
        return favorites.get().stream()
                .map(it -> toFavoriteResponse(it, stations))
                .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(Favorite favorite, List<Station> stations) {
        return FavoriteResponse.of(favorite.getId(), findStationById(favorite.getSource(), stations), findStationById(favorite.getTarget(), stations));
    }

    private Station findStationById(long stationId, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지하철역을 찾을 수 없습니다."));
    }
}