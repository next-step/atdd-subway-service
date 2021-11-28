package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.DataNotExistException;
import nextstep.subway.favorite.domain.FavoritePath;
import nextstep.subway.favorite.domain.FavoritePathRepository;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Service
public class FavoritePathService {
    private final FavoritePathRepository favoritePathRepository;
    private final MemberService memberService;
    private final PathService pathService;

    public FavoritePathService(FavoritePathRepository favoritePathRepository,
                               MemberService memberService,
                               PathService pathService) {
        this.favoritePathRepository = favoritePathRepository;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    public FavoritePathResponse createFavoritePath(LoginMember loginMember, FavoritePathRequest request) {
        Member member = memberService.findById(loginMember.getId());
        Station source = Station.from(request.getSourceId());
        Station target = Station.from(request.getTargetId());
        validateCreateFavoritePath(source, target);

        FavoritePath favoritePath = favoritePathRepository.save(FavoritePath.of(source, target, member));
        return FavoritePathResponse.from(favoritePath);
    }

    public List<FavoritePathResponse> findFavoritePaths(LoginMember loginMember) {
        List<FavoritePath> favoritePaths = favoritePathRepository.findAllByMemberId(loginMember.getId());
        return StreamUtils.mapToList(favoritePaths, FavoritePathResponse::from);
    }

    public FavoritePathResponse findFavoritePathById(Long id) {
        return favoritePathRepository.findById(id)
                                     .map(FavoritePathResponse::from)
                                     .orElseThrow(DataNotExistException::new);
    }

    public void deleteFavoritePath(Long id) {
        favoritePathRepository.deleteById(id);
    }

    private void validateCreateFavoritePath(Station source, Station target) {
        pathService.findShortestPath(source.getId(), target.getId());
    }
}
