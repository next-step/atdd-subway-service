package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final String MESSAGE_FAVORITE_ENTITY_NOT_FOUND = "즐겨찾기가가 존재하지 않습니다";

    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;
    private PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           MemberService memberService,
                           StationService stationService,
                           PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.pathService = pathService;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Long sourceStationId = favoriteRequest.getSourceId();
        Long targetStationId = favoriteRequest.getTargetId();

        checkSourceTargetHasPath(memberId, sourceStationId, targetStationId);

        Favorite favorite = saveFavoriteEntity(memberId, sourceStationId, targetStationId);
        return FavoriteResponse.of(favorite);
    }

    private Favorite saveFavoriteEntity(Long memberId, Long sourceStationId, Long targetStationId) {
        Member member = findMember(memberId);
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        return favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
    }

    private void checkSourceTargetHasPath(Long memberId, Long sourceStationId, Long targetStationId) {
        pathService.findShortestPath(memberId, sourceStationId, targetStationId);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = findMember(memberId);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavoriteById(Long memberId, Long favoriteId) {
        Member member = findMember(memberId);
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, member.getId()).orElseThrow(
            () -> new EntityNotFoundException(MESSAGE_FAVORITE_ENTITY_NOT_FOUND)
        );
        favoriteRepository.delete(favorite);
    }

    private Member findMember(Long memberId) {
        return memberService.findMemberEntity(memberId);
    }
}
