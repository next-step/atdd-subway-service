package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static nextstep.subway.common.Messages.NOT_EQUALS_MEMBER;

@Service
@Transactional
public class FavoriteService {

    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(
            MemberService memberService,
            FavoriteRepository favoriteRepository,
            StationService stationService
    ) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(memberId);
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());

        Favorite favorite = Favorite.of(member, sourceStation, targetStation);
        Favorite favoriteResult = favoriteRepository.save(favorite);

        return FavoriteResponse.of(favoriteResult);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void removeFavorites(Long memberId, Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);

        if (!favorite.isMemberFavorite(memberId)) {
            throw new AuthorizationException(NOT_EQUALS_MEMBER);
        }

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(NoSuchElementException::new);
    }
}
