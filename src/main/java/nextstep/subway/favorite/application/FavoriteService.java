package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.application.AuthorizationException;
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
@Transactional
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberService memberService;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long id, FavoriteRequest request) {
        Member member = memberService.findMemberById(id);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = favoriteRepository.save(request.toFavorite(member, source, target));
        return FavoriteResponse
            .of(favorite, StationResponse.of(source), StationResponse.of(target));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(Long id) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(id);
        return favorites.stream()
            .map(favorite -> FavoriteResponse.of(
                favorite,
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())))
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long loginMemberId, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        favorite.checkAuthority(loginMemberId);
        favoriteRepository.delete(favorite);
    }
}
