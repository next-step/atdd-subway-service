package nextstep.subway.favorite.application;

import nextstep.subway.common.exception.NotFoundEntityException;
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
    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse findFavoriteById(Long id) {
        return FavoriteResponse.of(findById(id));
    }

    private Favorite findById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(NotFoundEntityException::new);
    }

    public FavoriteResponse addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findById(memberId);
        Station sourceStation = stationService.findStationById(favoriteRequest.getSourceId());
        Station targetStation = stationService.findStationById(favoriteRequest.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findAllFavoritesByMemberId(Long memberId) {
        Member member = memberService.findById(memberId);
        List<Favorite> members = favoriteRepository.findAllByMember(member);
        return FavoriteResponse.listOf(members);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
