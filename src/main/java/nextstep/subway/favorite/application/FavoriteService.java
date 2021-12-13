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

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Long createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(memberId);

        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());

        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        Favorite saveFavorite = favoriteRepository.save(favorite);

        return saveFavorite.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return FavoriteResponse.ofList(favorites);
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
