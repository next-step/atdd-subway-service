package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.exception.NotFoundException;
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

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationService stationService,
        MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findStation(favoriteRequest.getSource());
        Station targetStation = stationService.findStation(favoriteRequest.getTarget());
        Member loginMember = memberService.findMember(memberId);

        List<Favorite> favorites = favoriteRepository.findAllByOwnerId(memberId);

        Favorite favorite = Favorite.of(sourceStation, targetStation, loginMember);
        favorite.validateIncludeFavorite(favorites);
        Favorite persist = favoriteRepository.save(favorite);

        return FavoriteResponse.of(persist);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavoriteResponseList(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByOwnerId(memberId);
        return FavoriteResponse.ofList(favorites);
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);

        Member member = memberService.findMember(memberId);

        System.out.println(member);
        System.out.println(favorite);
        favorite.validateDelete(member);

        favoriteRepository.delete(favorite);
    }

    private Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
            .orElseThrow(NotFoundException::new);
    }
}
