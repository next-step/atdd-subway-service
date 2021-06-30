package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private static final String FAVORITE_NOT_EXIST = "존재하지 않는 즐겨찾기";
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Favorite saveFavorite(Long loginMemberId, Long source, Long target) {
        return favoriteRepository.save(new Favorite(loginMemberId, source, target));
    }

    public List<FavoriteResponse> getFavorites(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMemberId);
        Set<Long> stationIds = getStationIds(favorites);
        Map<Long, Station> stations = getStations(stationIds);
        return toFavoriteResponse(favorites, stations);
    }

    @Transactional
    public void deleteFavorite(Long loginMemberId, Long favoriteId) {
        Favorite findFavorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMemberId)
                .orElseThrow(() -> new FavoriteNotFoundException(FAVORITE_NOT_EXIST));
        favoriteRepository.delete(findFavorite);
    }

    private List<FavoriteResponse> toFavoriteResponse(List<Favorite> favorites, Map<Long, Station> stations) {
        return favorites.stream()
                .map(v -> {
                            final Station sourceStation = stations.get(v.getSourceStationId());
                            final Station targetStation = stations.get(v.getTargetStationId());
                            return new FavoriteResponse(v, StationResponse.of(sourceStation), StationResponse.of(targetStation));
                        }
                )
                .collect(Collectors.toList());
    }

    private Map<Long, Station> getStations(Set<Long> stationIds) {
        return stationRepository.findAllById(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> getStationIds(List<Favorite> favorites) {
        Set<Long> stationsId = new HashSet<>();
        favorites.forEach(v -> {
            stationsId.add(v.getSourceStationId());
            stationsId.add(v.getTargetStationId());
        });
        return stationsId;
    }
}
