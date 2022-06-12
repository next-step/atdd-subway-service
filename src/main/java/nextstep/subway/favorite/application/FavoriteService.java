package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteReqeust;
import nextstep.subway.favorite.dto.FavoriteResponse;
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
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Member member, FavoriteReqeust reqeust) {
        Station source = stationService.findById(reqeust.getSourceId());
        Station target = stationService.findById(reqeust.getTargetId());
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavoritesByMemberId(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);

        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavoriteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}
