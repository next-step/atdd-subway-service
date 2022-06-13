package nextstep.subway.favorite.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(toList());
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteCreateRequest request) {
        Station source = stationService.findStationById(request.getSourceStationId());
        Station target = stationService.findStationById(request.getTargetStationId());
        Favorite newFavorite = favoriteRepository.save(new Favorite(memberId, source, target));
        return FavoriteResponse.of(newFavorite);
    }

    public void deleteFavorite(Long favoriteId) {
        Optional<Favorite> deleteTarget = favoriteRepository.findById(favoriteId);
        if (deleteTarget.isPresent()) {
            favoriteRepository.delete(deleteTarget.get());
        }
    }
}
