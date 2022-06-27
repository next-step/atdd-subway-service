package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
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
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional
    public FavoriteResponse create(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findAll(LoginMember loginMember) {
        Member member = memberService.findMemberById(loginMember.getId());
        return FavoriteResponse.of(favoriteRepository.findByMember(member));
    }

    @Transactional
    public void deleteById(LoginMember loginMember, Long id) {
        Member member = memberService.findMemberById(loginMember.getId());
        favoriteRepository.deleteByIdAndMember(id, member);
    }
}
