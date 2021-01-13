package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(StationService stationService, MemberService memberService) {
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public Favorite saveFavorite(Long memberId, FavoriteRequest request) {
        Station sourceStation = stationService.findById(request.getSourceId());
        Station targetStation = stationService.findById(request.getTargetId());
        Favorite favorite = new Favorite(sourceStation, targetStation);
        Member member = memberService.findMemberById(memberId);

        member.addFavorite(favorite);
        return favorite;
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return FavoriteResponse.ofList(member.getFavorites());
    }
}
