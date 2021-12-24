package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.application.PathService;
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
    private final MemberRepository memberRepository;
    private final StationService stationService;
    private final PathService pathService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationService stationService, PathService pathService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMember(loginMember);
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        pathService.findPath(loginMember, favoriteRequest.getSource(), favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        member.addFavorite(favorite);
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        return findMember(loginMember)
                .getFavorites()
                .stream()
                .map(favorite -> FavoriteResponse.of(favorite))
                .collect(Collectors.toList());
    }

    public void removeFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 존재하지 않습니다. ID : " + favoriteId));
        findMember(loginMember).removeFavorite(favorite);
    }

    public Member findMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
