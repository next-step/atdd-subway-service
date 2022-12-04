package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
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

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService,
                           FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMember(loginMember.getId());
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public List<FavoriteResponse> findAllFavorites(LoginMember loginMember) {
        Member member = memberService.findMember(loginMember.getId());
        List<Favorite> favorites = member.getFavorites();

        return favorites.stream()
                .map(favorite -> new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
