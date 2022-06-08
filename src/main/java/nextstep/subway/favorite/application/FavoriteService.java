package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           MemberService memberService,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Favorite createFavorite(LoginMember loginMember, Long sourceId, Long targetId){
        Member member = memberService.findMemberById(loginMember.getId());
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        return favoriteRepository.save(new Favorite(source,target,member));
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember){
        Member member = memberService.findMemberById(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
