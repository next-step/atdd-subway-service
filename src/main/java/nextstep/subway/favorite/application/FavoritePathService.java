package nextstep.subway.favorite.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoritePath;
import nextstep.subway.favorite.domain.FavoritePathRepository;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class FavoritePathService {
    private final FavoritePathRepository favoritePathRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoritePathService(FavoritePathRepository favoritePathRepository,
                               StationService stationService,
                               MemberService memberService) {
        this.favoritePathRepository = favoritePathRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoritePathResponse createFavoritePath(LoginMember loginMember, FavoritePathRequest request) {
        Member member = memberService.findById(loginMember.getId());
        Station source = stationService.findById(request.getSourceId());
        Station target = stationService.findById(request.getTargetId());
        FavoritePath favoritePath = favoritePathRepository.save(FavoritePath.of(source, target, member));

        return FavoritePathResponse.from(favoritePath);
    }
}
