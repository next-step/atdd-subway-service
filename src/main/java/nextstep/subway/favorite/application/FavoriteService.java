package nextstep.subway.favorite.application;

import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(memberId);
        Station sourceStation = findStation(favoriteRequest.getSourceId());
        Station targetStation = findStation(favoriteRequest.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.of(favorite);
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

    public void deleteFavorite(Long memberId, Long favoriteId) {
        favoriteRepository.deleteByMemberIdAndId(memberId, favoriteId);
    }
}
