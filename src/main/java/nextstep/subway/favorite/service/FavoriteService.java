package nextstep.subway.favorite.service;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NotFoundException;
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
public class FavoriteService {
    private static final String ERROR_MESSAGE_NOT_FOUND_FAVORITE = "즐겨찾기가 존재하지 않습니다. Favorite ID : %d";

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavoritesByMemberId(LoginMember loginMember) {
        Member member = memberService.findMemberById(loginMember.getId());
        return member.favorites().stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Favorite createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station departureStation = stationService.findStationById(favoriteRequest.getDepartureId());
        Station arrivalStation = stationService.findStationById(favoriteRequest.getArrivalId());
        return favoriteRepository.save(Favorite.of(member, departureStation, arrivalStation));
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Member member = memberService.findMemberById(loginMember.getId());
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_FAVORITE, favoriteId));
        member.removeFavorite(favorite);
    }
}
