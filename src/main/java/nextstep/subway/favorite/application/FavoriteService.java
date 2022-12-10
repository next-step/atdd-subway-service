package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
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
    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse save(Long loginMemberId, Long sourceId, Long targetId) {
        Member member = memberService.findById(loginMemberId);
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        return FavoriteResponse.from(favoriteRepository.save(new Favorite(member, source, target)));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findByMember(Long loginMemberId) {
        Member member = memberService.findById(loginMemberId);
        return favoriteRepository.findByMember(member)
            .stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }

    public void deleteByIdAndMember(Long id, Long loginMemberId) {
        Member member = memberService.findById(loginMemberId);
        favoriteRepository.findByIdAndMember(id, member)
            .orElseThrow(() -> new IllegalArgumentException(String.format("삭제 요청한 즐겨찾기를 찾을 수 없습니다. favoriteId=%d, memberId=%d", id, loginMemberId)));
        favoriteRepository.deleteByIdAndMember(id, member);
    }
}
