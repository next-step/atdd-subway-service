package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final StationService stationService;

    private final MemberService memberService;

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService,
                           FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse save(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findByEmail(loginMember.getEmail());
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member.getId(), source, target));
        return FavoriteResponse.of(favorite);
    }
}
