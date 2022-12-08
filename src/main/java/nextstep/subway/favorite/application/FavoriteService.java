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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
            MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public long create(FavoriteRequest request, LoginMember loginMember) {
        final Station source = stationService.findById(request.getSource());
        final Station target = stationService.findById(request.getTarget());
        final Member member = memberService.findRealMember(loginMember.getId());

        return favoriteRepository.save(new Favorite(source, target, member)).getId();
    }

    @Transactional(readOnly = true)
    public FavoriteResponse get(LoginMember loginMember) {
        final Member member = memberService.findRealMember(loginMember.getId());
        return new FavoriteResponse(favoriteRepository.findAllByMember(member));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(long id, LoginMember loginMember) {
        final Member member = memberService.findRealMember(loginMember.getId());
        favoriteRepository.findByIdAndMember(id, member)
                .ifPresent(it -> favoriteRepository.deleteById(it.getId()));
    }
}
