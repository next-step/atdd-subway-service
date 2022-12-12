package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           MemberService memberService,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public Long createFavorite(LoginMember loginMember, FavoriteCreateRequest request) {
        Favorite favorite = favoriteRepository.save(mapToFavorite(loginMember, request));
        return favorite.getId();
    }

    public List<FavoriteResponse> findAllFavoritesBy(LoginMember loginMember) {
        Member member = memberService.findMemberByLoginMember(loginMember);
        return favoriteRepository.findAllWithStationsByMember(member).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Member member = memberService.findMemberByLoginMember(loginMember);
        Favorite favorite = findFavoriteByIdAndMember(favoriteId, member);
        favoriteRepository.delete(favorite);
    }

    private Favorite mapToFavorite(LoginMember loginMember, FavoriteCreateRequest request) {
        Member member = memberService.findMemberByLoginMember(loginMember);
        Station targetStation = stationService.findStationById(request.getSource());
        Station sourceStation = stationService.findStationById(request.getTarget());
        return new Favorite(member, targetStation, sourceStation);
    }

    private Favorite findFavoriteByIdAndMember(Long favoriteId, Member member) {
        return favoriteRepository.findByIdAndMember(favoriteId, member)
                .orElseThrow(EntityNotFoundException::new);
    }
}
