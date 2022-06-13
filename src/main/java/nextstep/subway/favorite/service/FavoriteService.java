package nextstep.subway.favorite.service;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
        MemberService memberService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
        this.pathService = pathService;
    }

    public Favorite saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        PathResponse path = pathService.findPath(favoriteRequest.getSource(),
            favoriteRequest.getTarget());

        Member member = memberService.findById(memberId);
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = new Favorite(sourceStation, targetStation, member);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> getFavoriteList(Long memberId) {
        Member member = memberService.findById(memberId);
        return favoriteRepository.findAllByMember(member)
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, Long memberId) {
        Member member = memberService.findById(memberId);
        Favorite favorite = favoriteRepository.findByMemberAndId(member, id)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 즐겨찾기를 찾을수 없습니다"));
        favoriteRepository.delete(favorite);
    }
}
