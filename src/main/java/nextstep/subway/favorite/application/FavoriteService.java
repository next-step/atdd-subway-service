package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.primitives.Longs.asList;
import static java.lang.String.format;

@Service
public class FavoriteService {

    private final StationService stationService;

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse insertFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Stations stations = stationService.findAllById(asList(favoriteRequest.getSource(), favoriteRequest.getTarget()));
        Station source = stations.getById(favoriteRequest.getSource());
        Station target = stations.getById(favoriteRequest.getTarget());
        return FavoriteResponse.of(favoriteRepository.save(new Favorite(loginMember.getId(), source, target)));
    }

    public List<FavoriteResponse> findFavorite(Long id) {
        List<Favorite> favorites= favoriteRepository.findByMemberId(id);

        return favorites.stream()
                .map((favorite -> FavoriteResponse.of(favorite)))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        Favorite foundFavorite= favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(format("id가 %d인 즐겨찾기를 찾을 수가 없습니다.", id)));
        favoriteRepository.delete(foundFavorite);
    }
}
