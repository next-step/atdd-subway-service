package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.FindFailedException;
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
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Favorite favorite = new Favorite(
                findStationById(favoriteRequest.getSource()), findStationById(favoriteRequest.getTarget()));
        favorite.changeMember(findMemberById(loginMember.getId()));
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(savedFavorite, savedFavorite.getSourceStation(), savedFavorite.getTargetStation());
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return favoriteRepository.findAllByMember(findMemberById(loginMember.getId()))
                .stream()
                .map(it -> FavoriteResponse.of(it, it.getSourceStation(), it.getTargetStation()))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new FindFailedException(Favorite.class));
        favorite.validateOwner(findMemberById(loginMember.getId()));
        favoriteRepository.deleteById(id);
    }

    private Member findMemberById(Long id) {
        return memberService.findMemberById(id);
    }

    private Station findStationById(Long id) {
        return stationService.findStationById(id);
    }
}
