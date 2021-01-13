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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long saveFavorite(Long memberId, FavoriteRequest request) {
        Station sourceStation = stationService.findById(request.getSourceId());
        Station targetStation = stationService.findById(request.getTargetId());
        Member member = findMember(memberId);

        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return favorite.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = findMember(memberId);
        return FavoriteResponse.ofList(member.getFavorites());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = findMember(memberId);
        member.deleteFavorite(favoriteId);
    }

    private Member findMember(Long memberId) {
        return memberService.findMemberById(memberId);
    }
}
