package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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
        Member member = memberService.findById(loginMember.getId());
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findByMemberId(Long memberId) {
        return FavoriteResponse.ofList(favoriteRepository.findByMemberId(memberId));
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);
        if (!favorite.isOwner(memberId)) {
            throw new NotMineFavoriteException("자신의 즐겨찾기만 삭제 가능합니다");
        }
        favoriteRepository.delete(favorite);
    }
}
