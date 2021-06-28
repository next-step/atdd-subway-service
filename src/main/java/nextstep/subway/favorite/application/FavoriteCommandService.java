package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberQueryService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationQueryService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteCommandService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteQueryService favoriteQueryService;
    private final MemberQueryService memberQueryService;
    private final StationQueryService stationQueryService;

    public FavoriteCommandService(FavoriteRepository favoriteRepository, FavoriteQueryService favoriteQueryService, MemberQueryService memberQueryService, StationQueryService stationQueryService) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteQueryService = favoriteQueryService;
        this.memberQueryService = memberQueryService;
        this.stationQueryService = stationQueryService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberQueryService.findMemberByEmail(loginMember.getEmail());
        Station sourceStation = stationQueryService.findStationById(request.getSource());
        Station targetStation = stationQueryService.findStationById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return FavoriteResponse.of(favorite);
    }

    public void deleteByIdAndLoginMember(Long id, LoginMember loginMember) {
        Favorite favorite = favoriteQueryService.findById(id);
        favorite.checkOwner(loginMember);
        favoriteRepository.delete(favorite);
    }
}
