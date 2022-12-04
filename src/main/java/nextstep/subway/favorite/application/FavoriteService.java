package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository,
                           MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }


    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMember(loginMember);
        Station source = findStation(favoriteRequest.getSource());
        Station target = findStation(favoriteRequest.getTarget());

        Favorite favorite = Favorite.of(member, source, target);
        member.addFavorite(favorite);
        return FavoriteResponse.from(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMember(loginMember);
        return member.getFavorites()
                .stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = findMember(loginMember);
        Favorite favorite = findFavorite(id);
        member.deleteFavorite(favorite);
    }

    private Favorite findFavorite(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 찾을 수 없습니다."));
    }

    private Station findStation(Long favoriteRequest) {
        return stationRepository.findById(favoriteRequest)
                .orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다."));
    }

    private Member findMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new AuthorizationException("사용자를 찾을 수 없습니다."));
    }
}
