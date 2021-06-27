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
@Transactional
public class FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteQueryService favoriteQueryService;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteCommandService(FavoriteRepository favoriteRepository, FavoriteQueryService favoriteQueryService, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteQueryService = favoriteQueryService;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findByEmail(loginMember.getEmail());
        Station sourceStation = stationService.findById(Long.parseLong(request.getSource()));
        Station targetStation = stationService.findById(Long.parseLong(request.getTarget()));
        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return FavoriteResponse.of(favorite);
    }

    public void deleteByIdAndLoginMember(Long id, LoginMember loginMember) {
        Favorite favorite = favoriteQueryService.findById(id);
        favorite.checkOwner(loginMember);
        favoriteRepository.delete(favorite);
    }
}
