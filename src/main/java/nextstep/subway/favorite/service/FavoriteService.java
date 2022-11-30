package nextstep.subway.favorite.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station departureStation = stationService.findStationById(favoriteRequest.getDepartureId());
        Station arrivalStation = stationService.findStationById(favoriteRequest.getArrivalId());
        return favoriteRepository.save(Favorite.of(member, departureStation, arrivalStation));
    }
}
