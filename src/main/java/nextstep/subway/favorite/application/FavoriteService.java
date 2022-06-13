package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station sourceStation = stationService.findStationById(request.getSourceStationId());
        Station targetStation = stationService.findStationById(request.getTargetStationId());
        Member findMember = memberService.findById(loginMember.getId());

        Favorite favorite = new Favorite(findMember, sourceStation, targetStation);
        Favorite savedFavorite = favoriteRepository.save(favorite);

        return new FavoriteResponse(savedFavorite.getId(),
                StationResponse.of(savedFavorite.getSourceStation()),
                StationResponse.of(savedFavorite.getTargetStation())
        );
    }
}
