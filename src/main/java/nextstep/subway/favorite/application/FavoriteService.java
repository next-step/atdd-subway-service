package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final PathService pathService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(PathService pathService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.pathService = pathService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());

        if (!pathService.isValidatePath(sourceStation, targetStation)) {
            throw new FavoriteException("유효하지 않은 경로를 즐겨찾기로 등록 요청하였습니다.");
        }

        Favorite favorite = favoriteRepository.save(Favorite.of(loginMember.getId(), sourceStation, targetStation));

        return FavoriteResponse.toFavorite(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorite(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());

        return favorites.stream()
                .map(FavoriteResponse::toFavorite)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long targetId) {
        Favorite favorite = findFavoriteById(targetId);

        if (!favorite.isOwner(loginMember)) {
            throw new FavoriteException("삭제 요청한 즐겨찾기는 로그인한 사용자의 소유가 아닙니다.");
        }

        favoriteRepository.deleteById(targetId);
    }

    private Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteException("존재하지 않는 즐겨찾기 입니다."));
    }
}
