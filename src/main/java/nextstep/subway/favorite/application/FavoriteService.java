package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse create(Long memberId, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());
        Member member = memberService.findById(memberId);
        return FavoriteResponse.of(favoriteRepository.save(Favorite.of(member, sourceStation, targetStation)));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findResponse(Long memberId) {
        return find(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long id, LoginMember loginMember) {
        checkMyFavorite(id, loginMember);
        favoriteRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    private List<Favorite> find(Long memberId) {
        return favoriteRepository.findByMemberId(memberId);
    }
    
    private void checkMyFavorite(Long id, LoginMember loginMember) {
        List<Favorite> favorites = find(loginMember.getId());
        favorites.stream()
            .filter(favorite -> favorite.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("본인의 즐겨찾기만 삭제할 수 있습니다"));
    }
}
