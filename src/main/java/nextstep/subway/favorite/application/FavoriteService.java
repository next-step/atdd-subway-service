package nextstep.subway.favorite.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final Member loginMember, final FavoriteRequest request) {
        final Station source = stationService.findStationById(request.getSource());
        final Station target = stationService.findStationById(request.getTarget());
        final Favorite persistFavorite = favoriteRepository.save(Favorite.of(loginMember, source, target));
        return FavoriteResponse.from(persistFavorite);
    }

    public FavoriteResponse findLineResponseById(final Member loginMember, final Long id) {
        final Favorite favorite = favoriteRepository.findByIdAndMember(id, loginMember)
                .orElseThrow(NotFoundException::new);
        return FavoriteResponse.from(favorite);
    }

    @Transactional
    public void deleteLineById(final Member loginMember, final Long id) {
        favoriteRepository.deleteByIdAndMember(id, loginMember);
    }
}
