package nextstep.subway.favorite.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
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
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationService stationService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            MemberRepository memberRepository,
            StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse save(Long memberId, FavoriteRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(favorite.getId(), source, target);
    }

    public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {
        return FavoriteResponse.toList(favoriteRepository.findByMemberId(memberId));
    }

    @Transactional
    public void deleteById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
