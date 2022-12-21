package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Member member = memberService.findMemberById(memberId);
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        return FavoriteResponse.from(member.addFavorite(source, target));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);

        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }


    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberService.findMemberById(memberId);
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException(HttpStatus.NOT_FOUND.toString()));

        member.removeFavorite(favorite);
    }
}
