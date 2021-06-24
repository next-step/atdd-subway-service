package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.favorite.NotFoundAnyThingException;
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

    private MemberService memberService;
    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService,
            StationService stationService) {

        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findById(loginMember.getId());
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());
        Favorite favorite = Favorite.of(member, source, target);
        Favorite persist = favoriteRepository.save(favorite);

        return persist.getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        return favoriteRepository.findAllByMember(member)
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(NotFoundAnyThingException::new);
        favoriteRepository.delete(favorite);
    }

}
