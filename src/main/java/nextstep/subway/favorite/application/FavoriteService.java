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
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService,
                           FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavoriteOfMine(Long memberId, FavoriteRequest favoriteRequest) {
        final Member mine = memberService.findMemberById(memberId);
        final Station source = stationService.findStationById(favoriteRequest.getSourceId());
        final Station target = stationService.findStationById(favoriteRequest.getTargetId());

        Favorite persistFavorite = favoriteRepository.save(new Favorite(mine, source, target));
        return persistFavorite.toFavoriteResponse();
    }

    public List<FavoriteResponse> findFavoriteOfMine(Long memberId) {
        final Member mine = memberService.findMemberById(memberId);

        List<Favorite> favorites = favoriteRepository.findAllByMemberId(mine.getId());
        return favorites.stream()
                .map(Favorite::toFavoriteResponse)
                .collect(Collectors.toList());
    }

    public void deleteFavoriteOfMine(Long memberId, Long favoriteId) {
    }
}
