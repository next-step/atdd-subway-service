package nextstep.subway.favorite.application;

import nextstep.subway.exception.CannotFindException;
import nextstep.subway.exception.Message;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Member member = memberService.findById(memberId);
        Map<Long, Station> stations = stationService.findStations(request.getSource(), request.getTarget());
        Station source = stations.get(request.getSource());
        Station target = stations.get(request.getTarget());

        Favorite favorite = new Favorite(member, source, target);

        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        return FavoriteResponse.ofList(favoriteRepository.findByMemberId(id));
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, memberId)
                .orElseThrow(() -> new CannotFindException(Message.ERROR_CANNOT_FIND_FAVORITE));
        favoriteRepository.delete(favorite);
    }
}
