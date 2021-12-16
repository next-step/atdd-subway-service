package nextstep.subway.favorite.application;

import nextstep.subway.exception.BadRequestException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(memberId);
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        return FavoriteResponse.of(favoriteRepository.save(Favorite.of(source, target, member)));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavoritesByMember(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Favorite> favorites = favoriteRepository.findFavoritesByMemberOrderById(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new BadRequestException("즐겨찾기가 삭제됐거나, 존재하지 않습니다."));
    }

    public void removeFavorite(Long memberId, Long favoriteId) {
        Member member = memberService.findMemberById(memberId);
        Favorite favorite = findFavoriteById(favoriteId);
        if(favorite.isMember(member)) {
            favoriteRepository.delete(favorite);
        }
    }
}
