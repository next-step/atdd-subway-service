package nextstep.subway.domain.favorite.application;

import nextstep.subway.domain.auth.domain.User;
import nextstep.subway.domain.favorite.domain.Favorite;
import nextstep.subway.domain.favorite.domain.FavoriteRepository;
import nextstep.subway.domain.favorite.dto.FavoriteRequest;
import nextstep.subway.domain.favorite.dto.FavoriteResponse;
import nextstep.subway.domain.member.application.MemberService;
import nextstep.subway.domain.member.domain.Member;
import nextstep.subway.domain.station.application.StationService;
import nextstep.subway.domain.station.domain.Station;
import nextstep.subway.global.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse createFavorite(User user, FavoriteRequest request) {
        Member member = findMember(user);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return new FavoriteResponse(favorite.getId());
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorite(User user) {
        Member member = findMember(user);
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    private Member findMember(User user) {
        return memberService.findMember(user);
    }

    public void deleteFavorite(User user, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(EntityNotFoundException::new);

        favorite.checkOwner(findMember(user));

        favoriteRepository.delete(favorite);
    }
}
