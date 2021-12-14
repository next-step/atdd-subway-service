package nextstep.subway.domain.favorite.application;

import nextstep.subway.domain.auth.domain.LoginMember;
import nextstep.subway.domain.favorite.domain.Favorite;
import nextstep.subway.domain.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.favorite.dto.FavoriteRequest;
import nextstep.subway.domain.favorite.dto.FavoriteResponse;
import nextstep.subway.domain.member.application.MemberService;
import nextstep.subway.domain.member.domain.Member;
import nextstep.subway.domain.station.application.StationService;
import nextstep.subway.domain.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMember(loginMember);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return new FavoriteResponse(favorite.getId());
    }
}
