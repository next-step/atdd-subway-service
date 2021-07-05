package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
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

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long id, FavoriteRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Station source = findStationById(request.getSourceId());
        Station target = findStationById(request.getTargetId());

        Favorite savedFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(savedFavorite);
    }

    public List<FavoriteResponse> findAll(Long id) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(id);
        return getFavoriteResponses(favorites);
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 존재하지 않습니다."));
        if (!favorite.hasSameMemberId(memberId)) {
            throw new AuthorizationException("유효하지 않은 사용자입니다.");
        }
        favoriteRepository.delete(favorite);
    }

    private List<FavoriteResponse> getFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    private Station findStationById(Long targetId) {
        return stationRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역 입니다."));
    }
}
