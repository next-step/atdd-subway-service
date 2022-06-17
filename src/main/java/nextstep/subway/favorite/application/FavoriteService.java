package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteRequest;
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
            FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationService stationService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public Favorites getAllFavoriteByOwner(Long creatorId) {
        return new Favorites(favoriteRepository.findAllByCreatorId(creatorId));
    }

    @Transactional
    public Favorite saveFavorite(FavoriteRequest request, Long memberId) {
        Member member = memberRepository.getById(memberId);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        return favoriteRepository.save(new Favorite(source, target, member));
    }
}
