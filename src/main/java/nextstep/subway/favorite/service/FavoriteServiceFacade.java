package nextstep.subway.favorite.service;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteServiceFacade {

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteService favoriteService;

    public FavoriteServiceFacade(StationService stationService, MemberService memberService,
        FavoriteService favoriteService) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteService = favoriteService;
    }

    @Transactional
    public Favorite saveFavorite(long memberId, long srcStationId, long dstStationId) {
        Member member = memberService.findById(memberId);
        Station srcStation = stationService.findById(srcStationId);
        Station dstStation = stationService.findById(dstStationId);

        Favorite favorite = new Favorite(srcStation, dstStation, member);
        return favoriteService.save(favorite);
    }

    public List<FavoriteResponse> getFavoriteList(long memberId) {
        Member member = memberService.findById(memberId);

        return favoriteService.findAllByMember(member).stream().map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, long memberId) {
        Member member = memberService.findById(memberId);
        Favorite favorite = favoriteService.findByMemberAndId(member, id);

        favoriteService.delete(favorite);
    }
}
