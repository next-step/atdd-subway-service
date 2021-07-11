package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.exception.UnapprovedException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorite(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());

        return favorites.stream()
                .map((FavoriteResponse::of))
                .collect(Collectors.toList());
    }

    public void deleteById(LoginMember loginMember, Long id) {
        Favorite foundFavorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException(id));

        if(!foundFavorite.hasPermission(loginMember.getId())) {
            throw new UnapprovedException(format("id가 %d인 사용자는 id가 %d인 즐겨찾기에 권한이 없습니다.", loginMember.getId(), id));
        }

        favoriteRepository.delete(foundFavorite);
    }
}
