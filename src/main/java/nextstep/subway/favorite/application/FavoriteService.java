package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private static final String FAVORITE_NOT_EXIST = "존재하지 않는 즐겨찾기";
    private static final String NOT_EXIST_STATION = "존재하지 않는 역: ";
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Favorite saveFavorite(Long loginMemberId, Long source, Long target) {
        final Station sourceStation = findStation(source);
        final Station targetStation = findStation(target);
        return favoriteRepository.save(new Favorite(loginMemberId, sourceStation, targetStation));
    }

    public List<FavoriteResponse> getFavorites(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMemberId);
        return favorites.stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long loginMemberId, Long favoriteId) {
        final Favorite findFavorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMemberId)
                .orElseThrow(() -> new FavoriteNotFoundException(FAVORITE_NOT_EXIST));
        favoriteRepository.delete(findFavorite);
    }

    private Station findStation(Long source) {
        return stationRepository.findById(source)
                .orElseThrow(() -> new StationNotFoundException(NOT_EXIST_STATION + source));
    }
}
