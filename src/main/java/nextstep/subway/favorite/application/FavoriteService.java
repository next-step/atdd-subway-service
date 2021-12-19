package nextstep.subway.favorite.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findById(memberId);
        Station sourceStation = stationService.findStationById(favoriteRequest.getSourceStationId());
        Station targetStation = stationService.findStationById(favoriteRequest.getTargetStationId());

        Favorite transientFavorite = new Favorite(member, sourceStation, targetStation);
        Favorite persistFavorite = favoriteRepository.save(transientFavorite);
        return FavoriteResponse.of(persistFavorite);
    }
}
