package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(memberId);
        Station sourceStation = stationService.findById(favoriteRequest.getSourceId());
        Station targetStation = stationService.findById(favoriteRequest.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites() {
        return null;
    }

    public void deleteFavoriteById(Long id) {
    }
}
