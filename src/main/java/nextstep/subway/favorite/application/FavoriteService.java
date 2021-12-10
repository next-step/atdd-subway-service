package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.exception.CannotAddException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
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

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findStation(favoriteRequest.getSource());
        Station targetStation = stationService.findStation(favoriteRequest.getTarget());

        List<Favorite> favorites = favoriteRepository.findAllByOwnerId(memberId);

        Favorite favorite = Favorite.of(sourceStation, targetStation, memberId);
        validateIncludeFavorite(favorite, favorites);
        Favorite persist = favoriteRepository.save(favorite);

        return FavoriteResponse.of(persist);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavoriteResponseList(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByOwnerId(memberId);
        return FavoriteResponse.ofList(favorites);
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);
        favorite.validateDelete(memberId);

        favoriteRepository.delete(favorite);
    }

    private Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
            .orElseThrow(NotFoundException::new);
    }

    private void validateIncludeFavorite(Favorite favorite, List<Favorite> favorites) {
        if (includeFavorite(favorite, favorites)) {
            throw new CannotAddException("이미 등록된 즐겨찾기 입니다.");
        }
    }

    private boolean includeFavorite(Favorite favorite, List<Favorite> favorites) {
        return favorites.stream()
            .anyMatch(favorite::equalsFavorite);
    }
}
