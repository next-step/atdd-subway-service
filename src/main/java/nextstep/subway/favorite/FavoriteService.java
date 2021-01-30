package nextstep.subway.favorite;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(MemberService memberService, FavoriteRepository favoriteRepository, StationService stationService) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse create(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Member member = memberService.getMember(loginMember.getId());
        Favorite favorite = new Favorite(source, target, member);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMember_Id(loginMember.getId());
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
