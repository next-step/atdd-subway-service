package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationService stationService,
        PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }


    public FavoriteResponse saveFavorite(LoginMember member, FavoriteRequest request) {
        Station source = station(request.getSource());
        Station target = station(request.getTarget());
        validatePath(source, target);
        return FavoriteResponse.from(savedFavorite(member, source, target));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember member) {
        return FavoriteResponse.listOf(favoriteRepository.findAllByMemberId(member.id()));
    }

    public void deleteFavorite(long id, LoginMember member) {
        favoriteRepository.delete(favorite(id, member));
    }

    private Favorite favorite(long id, LoginMember member) {
        return favoriteRepository.findByIdAndMemberId(id, member.id())
            .orElseThrow(() -> new NotFoundException(
                String.format("사용자(%s)가 저장한 즐겨찾기 id(%d)가 존재하지 않습니다.", member, id)));
    }

    private Favorite savedFavorite(LoginMember member, Station source, Station target) {
        return favoriteRepository.save(Favorite.from(source, target, member.id()));
    }

    private void validatePath(Station source, Station target) {
        try {
            pathService.shortestPath(source, target);
        } catch (InvalidDataException e) {
            throw new InvalidDataException(
                String.format("출발역(%s)과 도착역(%s) 경로를 찾을 수 없어 즐겨찾기를 저장할 수 없습니다.",
                    source, target),
                e);
        }
    }

    private Station station(long id) {
        return stationService.findById(id);
    }
}
