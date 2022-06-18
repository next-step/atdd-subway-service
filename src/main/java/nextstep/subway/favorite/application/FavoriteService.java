package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(StationService stationService, FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        validate(favoriteRequest);
        Favorite favorite = favoriteRepository.save(
                new Favorite(loginMemberId, favoriteRequest.getSource(), favoriteRequest.getTarget()));
        return favorite.getId();
    }

    private void validate(FavoriteRequest favoriteRequest) {
        stationService.findStationByIdOrElseThrow(favoriteRequest.getSource());
        stationService.findStationByIdOrElseThrow(favoriteRequest.getTarget());
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        Set<Station> stations = stationRepository.findFavoriteStationByMemberId(memberId);

        return favorites.stream()
                .map(f -> FavoriteResponse.of(
                        f.getId(),
                        getStationById(stations, f.getSourceStationId()),
                        getStationById(stations, f.getTargetStationId())
                       ))
                .collect(Collectors.toList());
    }

    private Station getStationById(Set<Station> stations, Long id) {
        return stations.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다"));
    }

    @Transactional
    public void deleteFavoriteOnlyMine(Long memberId, Long favoriteId) {
        Favorite favorite = findByIdOrElseThrow(favoriteId);
        if (!favorite.createdBy(memberId)) {
            throw new IllegalArgumentException("즐겨찾기 삭제 권한이 없습니다. memberId=" + memberId);
        }
        favoriteRepository.deleteByIdAndMemberId(favoriteId, memberId);
    }

    private Favorite findByIdOrElseThrow(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다. favoriteId=" + favoriteId));
    }
}
