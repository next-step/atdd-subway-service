package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    public Favorite saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMine(loginMember.getId());
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        return favoriteRepository.save(Favorite.of(member, source, target));
    }

    public List<FavoriteResponse> findFavorite(LoginMember loginMember) {
        List<Favorite> list = favoriteRepository.findByMemberId(loginMember.getId());
        return list.stream()
                .map(favorite -> FavoriteResponse.of(favorite.getId(),
                        StationResponse.of(favorite.getSource()),
                        StationResponse.of(favorite.getTarget()))
                ).collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);
        favorite.checkAuth(loginMember.getId());
        favoriteRepository.delete(favorite);
    }
}
