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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        Map<Long, Station> stations = getStations(request);

        Favorite favorite = new Favorite(loginMember.getId(), stations.get(request.getSource()), stations.get(request.getTarget()));
        favoriteRepository.save(favorite);
    }

    private Map<Long, Station> getStations(FavoriteRequest request) {
        return stationRepository.findAllById(Arrays.asList(request.getSource(), request.getTarget())).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private void validateSameStationId(FavoriteRequest request) {
        if (Objects.equals(request.getSource(), request.getTarget())) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다");
        }
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());

        return favorites.stream()
                .map(it -> FavoriteResponse.of(
                        it,
                        StationResponse.of(it.getSourceStation()),
                        StationResponse.of(it.getTargetStation())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 즐겨찾기 항목입니다."));
        validateOwner(loginMember, favorite);
        favoriteRepository.deleteById(id);
    }

    private void validateOwner(LoginMember loginMember, Favorite favorite) {
        if (!favorite.isCreatedBy(loginMember.getId())) {
            throw new HasNotPermissionException(loginMember.getId() + "는 삭제할 권한이 없습니다.");
        }
    }
}
