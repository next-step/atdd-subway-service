package nextstep.subway.favorite.application;

import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
  private final StationRepository stationRepository;
  private final FavoriteRepository favoriteRepository;

  public FavoriteService(StationRepository stationRepository, FavoriteRepository favoriteRepository) {
    this.stationRepository = stationRepository;
    this.favoriteRepository = favoriteRepository;
  }

  public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest request) {
    Station sourceStation = findStation(request.getSourceStationId());
    Station targetStation = findStation(request.getTargetStationId());
    return FavoriteResponse.of(favoriteRepository.save(new Favorite(memberId, sourceStation, targetStation)));
  }

  public List<FavoriteResponse> findFavorites(Long memberId) {
    return favoriteRepository.findAllByMemberId(memberId)
                          .stream()
                          .map(FavoriteResponse::of)
                          .collect(Collectors.toList());
  }

  public void deleteFavorite(Long favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }

  private Station findStation(Long stationId) {
    return stationRepository.findById(stationId)
            .orElseThrow(StationNotExistException::new);
  }
}
