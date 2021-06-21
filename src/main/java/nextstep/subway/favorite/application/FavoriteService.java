package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.favorite.domain.DeleteFavoriteException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.NotFoundFavoriteException;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService,
                           MemberService memberService,
                           FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public Long save(Long memberId, Long sourceId, Long targetId) {

        Member member = memberService.findById(memberId);
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        Favorite persistEntity = favoriteRepository.save(new Favorite(member, source, target));
        return persistEntity.getId();
    }

    public List<Favorite> findAllByMember(Long memberId) {
        Member member = memberService.findById(memberId);
        return favoriteRepository.findAllByOwner(member);
    }

    public void delete(Long memberId, Long favoriteId) {

        Favorite favorite = favoriteRepository.findById(favoriteId)
                                              .orElseThrow(NotFoundFavoriteException::new);

        if (favorite.isNotOwner(memberId)) {
            throw new DeleteFavoriteException("자신의 즐겨찾기만 삭제할 수 있습니다.");
        }

        favoriteRepository.deleteById(favoriteId);
    }
}
