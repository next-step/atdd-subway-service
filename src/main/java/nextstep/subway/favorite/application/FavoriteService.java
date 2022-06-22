package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final MemberService memberService, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest request) {
        final Member member = memberService.findMemberById(loginMember.getId());
        final Station sourceStation = stationService.findStationById(request.getSource());
        final Station targetStation = stationService.findStationById(request.getTarget());
        final Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return new FavoriteResponse(
                favorite.getId(),
                MemberResponse.of(member),
                StationResponse.of(sourceStation),
                StationResponse.of(targetStation));
    }
}
