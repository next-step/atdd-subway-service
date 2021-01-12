package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteReuqest;
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

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteReuqest param) {
        Member member = memberService.findById(memberId);
        Station source = stationService.findById(param.getSource());
        Station target = stationService.findById(param.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavoritesOfMember(Long memberId) {
        Member member = memberService.findById(memberId);
        return member.getFavorites().stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberService.findById(memberId);
        member.deleteFavorite(favoriteId);
    }
}
