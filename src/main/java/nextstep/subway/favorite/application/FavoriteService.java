package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Map;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.favorite.exception.NotMineFavoriteException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Favorite newFavorite = makeFavorite(loginMember.getId(), request.getSource(), request.getTarget());
        Favorite persistFavorite = favoriteRepository.save(newFavorite);
        return FavoriteResponse.of(persistFavorite);
    }

    private Favorite makeFavorite(long memberId, long sourceStationId, long targetStationId) {
        Member member = memberService.findById(memberId);
        Map<Long, Station> stationMap = stationService.findMapByIds(sourceStationId, targetStationId);
        Station source = stationMap.get(sourceStationId);
        Station target = stationMap.get(targetStationId);
        return new Favorite(member, source, target);
    }

    public List<FavoriteResponse> findByMemberId(Long memberId) {
        return FavoriteResponse.ofList(favoriteRepository.findByMemberId(memberId));
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, memberId)
            .orElseThrow(FavoriteNotFoundException::new);

        if (!favorite.isOwner(memberId)) {
            throw new NotMineFavoriteException("자신의 즐겨찾기만 삭제 가능합니다");
        }
        favoriteRepository.delete(favorite);
    }
}
