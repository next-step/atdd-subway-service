package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.exception.NotExistException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {
    private final StationService stationService;
    private final MemberService memberService;
    private final PathFinder pathFinder;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService,
                           PathFinder pathFinder, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.pathFinder = pathFinder;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavoriteOfMine(Long memberId, FavoriteRequest favoriteRequest) {
        final Member mine = memberService.findMemberById(memberId);
        final Station source = stationService.findStationById(favoriteRequest.getSourceId());
        final Station target = stationService.findStationById(favoriteRequest.getTargetId());
        validateFavorite(source, target);

        Favorite persistFavorite = favoriteRepository.save(new Favorite(mine, source, target));
        return persistFavorite.toFavoriteResponse();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavoriteOfMine(Long memberId) {
        final Member mine = memberService.findMemberById(memberId);

        List<Favorite> favorites = favoriteRepository.findAllByMemberId(mine.getId());
        return favorites.stream()
                .map(Favorite::toFavoriteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteOfMine(Long memberId, Long favoriteId) {
        final Member mine = memberService.findMemberById(memberId);

        Optional<Favorite> optionalFavorite = favoriteRepository.findByIdAndMemberId(favoriteId, mine.getId());
        favoriteRepository.delete(
                optionalFavorite.orElseThrow(() -> new NotExistException("해당 ID에 대한 즐겨찾기가 존재하지 않습니다."))
        );
    }

    private void validateFavorite(Station source, Station target) {
        pathFinder.validatePath(source, target);
    }
}
