package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.HasNotPermissionException;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createFavorite(LoginMember loginMember, FavoriteRequest request) {
        validateSameStationId(request);

        Favorite favorite = new Favorite(loginMember.getId(), request.getSource(), request.getTarget());
        favoriteRepository.save(favorite);
    }

    private void validateSameStationId(FavoriteRequest request) {
        if (Objects.equals(request.getSource(), request.getTarget())) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다");
        }
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        Map<Long, Station> stations = extractStations(favorites);

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(stations.get(it.getSourceStationId())),
                        StationResponse.of(stations.get(it.getTargetStationId()))))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> extractStations(List<Favorite> favorites) {
        Set<Long> stationIds = extractStationIds(favorites);
        return stationRepository.findAllById(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Long> extractStationIds(List<Favorite> favorites) {
        Set<Long> stationIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        return stationIds;
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        validateOwner(loginMember, favorite);
        favoriteRepository.deleteById(id);
    }

    private void validateOwner(LoginMember loginMember, Favorite favorite) {
        if (!favorite.isCreatedBy(loginMember.getId())) {
            throw new HasNotPermissionException(loginMember.getId() + "는 삭제할 권한이 없습니다.");
        }
    }
}
