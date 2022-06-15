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
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
                           MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        Favorite newFavorite = favoriteRepository.save(new Favorite.Builder()
                .member(member)
                .source(sourceStation)
                .target(targetStation)
                .build()
        );

        return FavoriteResponse.from(newFavorite);
    }

    public List<FavoriteResponse> findFavoriteResponses(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
