package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = request.toFavorite(memberId);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(savedFavorite.getId(), source, target);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        List<Long> stationIds = findStationIds(favorites);
        List<StationResponse> stations = stationService.findAllByIds(stationIds);
        return ofFavoriteResponse(favorites, stations);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private List<Long> findStationIds(List<Favorite> favorites) {
        return favorites.stream()
                .flatMap(favorite -> Stream.of(favorite.getSource(), favorite.getTarget()))
                .collect(Collectors.toList());
    }

    private List<FavoriteResponse> ofFavoriteResponse(List<Favorite> favorites, List<StationResponse> stations) {
        return favorites.stream()
                .map(it -> toFavoriteResponse(it, stations))
                .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(Favorite favorite, List<StationResponse> stations) {
        return FavoriteResponse.of(favorite.getId(), findStationResponseById(favorite.getSource(), stations), findStationResponseById(favorite.getTarget(), stations));
    }

    private StationResponse findStationResponseById(long stationId, List<StationResponse> stations) {
        return stations.stream()
                .filter(stationResponse -> stationResponse.getId() == stationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지하철역을 찾을 수 없습니다."));
    }
}
