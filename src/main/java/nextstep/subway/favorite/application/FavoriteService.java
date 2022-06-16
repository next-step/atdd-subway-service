package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
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
        Favorite favorite = favoriteRepository.save(new Favorite(member.getId(), source, target, member));
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorite(LoginMember loginMember) {
        Member member = memberService.findByEmail(loginMember.getEmail());
        List<Favorite> favoriteList = favoriteRepository.findByMemberId(member.getId());
        return favoriteList.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
