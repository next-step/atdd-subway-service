package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoritesResponse;
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
        Member mine = memberService.findMemberById(memberId);
        Station source = stationService.findStationById(favoriteRequest.getSourceId());
        Station target = stationService.findStationById(favoriteRequest.getTargetId());

        Favorite persistFavorite = favoriteRepository.save(new Favorite(mine, source, target));
        return persistFavorite.toFavoriteResponse();
    }

    public FavoritesResponse findFavoriteOfMine(Long memberId) {
        return new FavoritesResponse();
    }

    public void deleteFavoriteOfMine(Long memberId, Long favoriteId) {
    }
}
