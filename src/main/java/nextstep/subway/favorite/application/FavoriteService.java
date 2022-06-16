package nextstep.subway.favorite.application;

import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.NotAuthorizedException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository,
                           FavoriteRepository favoriteRepository,
                           StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(memberId);
        validateRequest(favoriteRequest);
        Station sourceStation = findStation(favoriteRequest.getSourceId());
        Station targetStation = findStation(favoriteRequest.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.of(favorite);
    }

    private void validateRequest(FavoriteRequest favoriteRequest) {
        if (Objects.equals(favoriteRequest.getSourceId(), favoriteRequest.getTargetId())) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원이 아닙니다."));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("지하철 역이 존재하지 않습니다."));
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 존재하지 않습니다."));

        if (!favorite.isCreatedBy(memberId)) {
            throw new NotAuthorizedException("삭제할 권한이 없습니다.");
        }
        favoriteRepository.delete(favorite);
    }
}
