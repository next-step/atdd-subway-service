package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public Long saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.getOne(loginMember.getId());
        Station sourceStation = stationService.getOne(request.getSource());
        Station targetStation = stationService.getOne(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return favorite.getId();
    }
}
