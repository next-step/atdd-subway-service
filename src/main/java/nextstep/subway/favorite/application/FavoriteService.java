package nextstep.subway.favorite.application;

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
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberOrElseThrow(loginMemberId);
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));

        return FavoriteResponse.of(favorite);

    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        return findFavoritesByMemberIdOrElseThrow(memberId)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public List<Favorite> findFavoritesByMemberIdOrElseThrow(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("id가 존재하지 않습니다. id=" + memberId));
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
