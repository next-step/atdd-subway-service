package nextstep.subway.favorite.application;

import nextstep.subway.favorite.FavoriteRequest;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(
            FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse create(long loginMemberId, FavoriteRequest request) {
        Member member = memberService.findById(loginMemberId);
        Station source = stationService.findStationById(request.getSourceStationId());
        Station target = stationService.findStationById(request.getTargetStationId());
        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> list(long loginMemberId) {
        Member member = memberService.findById(loginMemberId);
        List<Favorite> list = favoriteRepository.findAllByMember(member);
        return list.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void delete(long loginMemberId, long id) {
        Member member = memberService.findById(loginMemberId);
        favoriteRepository.deleteByMemberAndId(member, id);
    }
}
