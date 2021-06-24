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
import java.util.stream.Collectors;

@Transactional
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMemberById(loginMember.getId());
        Station source = findStationById(favoriteRequest.getSource());
        Station target = findStationById(favoriteRequest.getSource());

        Favorite favorite = new Favorite(member, source, target);
        Favorite saveFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(saveFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMemberById(loginMember.getId());
        return member.getFavorites().stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void removeFavorite(LoginMember loginMember, Long favoriteId) {
        Member member = findMemberById(loginMember.getId());
        Favorite favorite = findFavoriteById(favoriteId);
        member.removeFavorite(favorite);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 존재하지 않습니다."));
    }
}
