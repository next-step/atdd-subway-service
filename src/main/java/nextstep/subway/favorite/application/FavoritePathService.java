package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoritePath;
import nextstep.subway.favorite.domain.FavoritePathRepository;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.finder.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class FavoritePathService {
    private final FavoritePathRepository favoritePathRepository;
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoritePathService(FavoritePathRepository favoritePathRepository,
                               LineService lineService,
                               StationService stationService,
                               MemberService memberService) {
        this.favoritePathRepository = favoritePathRepository;
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoritePathResponse createFavoritePath(LoginMember loginMember, FavoritePathRequest request) {
        Member member = memberService.findById(loginMember.getId());
        Station source = stationService.findById(request.getSourceId());
        Station target = stationService.findById(request.getTargetId());

        validateCreateFavoritePath(source, target);

        FavoritePath favoritePath = favoritePathRepository.save(FavoritePath.of(source, target, member));
        return FavoritePathResponse.from(favoritePath);
    }

    private void validateCreateFavoritePath(Station source, Station target) {
        List<Line> lines = lineService.findAll();
        PathFinder pathFinder = PathFinder.from(lines);
        pathFinder.findShortestPath(source, target);
    }
}
