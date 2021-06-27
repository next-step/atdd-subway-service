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
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService,
            StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberEntity(loginMember.getId());
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        favoriteRepository.deleteByIdAndMemberId(id, loginMember.getId());
    }
}
