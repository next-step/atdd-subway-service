package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.FavoriteNotFoundException;
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
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public Favorite saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());
        Member member = memberService.findById(loginMember.getId());
        return favoriteRepository.save(new Favorite(member, source, target));
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteNotFoundException("없는 즐겨찾기입니다."));

        if (favorite.isNotOwner(loginMember.getId())) {
            throw new AuthorizationException("권한이 없습니다.");
        }
        favoriteRepository.delete(favorite);
    }
}
