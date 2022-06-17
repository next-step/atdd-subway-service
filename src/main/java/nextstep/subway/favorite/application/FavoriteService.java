package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMemberById(memberId);
        Station source = stationService.findStationById(favoriteRequest.getSourceStationId());
        Station target = stationService.findStationById(favoriteRequest.getTargetStationId());
        Favorite favorite = favoriteRepository.save(favoriteRequest.toFavorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorites(Long memberId) {
        return favoriteRepository.findAllByMemberId(memberId).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteById(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 즐겨찾기입니다."));

        if(!favorite.isCreateBy(memberId)) {
            throw new AuthorizationException("삭제 권한이 없습니다.");
        }

        favoriteRepository.delete(favorite);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
