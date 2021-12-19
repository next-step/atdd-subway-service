package nextstep.subway.favorite.application;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.NotFoundApiException;
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

@Transactional
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

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Favorite favorite = toFavorite(memberId, favoriteRequest);
        Favorite persistFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(persistFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> persistFavorites = favoriteRepository.findByMemberId(memberId);
        return persistFavorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite persistFavorite = findFavoriteById(favoriteId);
        persistFavorite.validateMember(memberId);

        favoriteRepository.delete(persistFavorite);
    }

    @Transactional(readOnly = true)
    public Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NotFoundApiException(ErrorCode.NOT_FOUND_FAVORITE_ID));
    }

    private Favorite toFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Member member = memberService.findMemberById(memberId);

        return Favorite.of(member, source, target);
    }
}
