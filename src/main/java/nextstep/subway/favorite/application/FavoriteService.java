package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
public class FavoriteService {

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberService memberService, final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite createFavorites(FavoriteRequest favoriteRequest, LoginMember loginMember) {

        Member member = memberService.findById(loginMember.getId());

        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        return favoriteRepository.save(Favorite.of(source, target, member));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(LoginMember loginMember) {

        Member member = memberService.findById(loginMember.getId());

        List<Favorite> favorites = favoriteRepository.findByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(toList());
    }

    @Transactional
    public void deleteFavorites(Long id, LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        favoriteRepository.deleteByIdAndMember(id, member);
    }
}
