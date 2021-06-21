package nextstep.subway.favorite.application;

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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public Favorite createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMember(loginMember);

        Station sourceStation = findStation(favoriteRequest.getSource());
        Station targetStation = findStation(favoriteRequest.getTarget());

        Favorite favorite = new Favorite(sourceStation, targetStation);
        member.addFavorite(favorite);

        return favorite;
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMember(loginMember);

        return member.getFavorites()
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(LoginMember loginMember, Long id) {
        Member member = findMember(loginMember);

        member.removeFavorite(findFavorite(id));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 전철역입니다."));
    }

    private Member findMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않은 사용자 입니다."));
    }

    private Favorite findFavorite(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 즐겨찾기 입니다."));
    }
}
