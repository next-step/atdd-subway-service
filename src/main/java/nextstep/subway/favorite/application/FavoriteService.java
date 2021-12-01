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

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(Long memberId, Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        Member member = memberService.findById(memberId);
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorites(Long memberId) {
        Member member = memberService.findById(memberId);
        return favoriteRepository.findAllByMember(member).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
