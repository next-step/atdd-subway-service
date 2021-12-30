package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.exception.PermissionException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
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
    public FavoriteResponse saveFavorite(final LoginMember loginMember, final FavoriteRequest request) {
        final Station source = stationService.findStationById(request.getSource());
        final Station target = stationService.findStationById(request.getTarget());
        final Favorite persistFavorite = favoriteRepository.save(Favorite.of(loginMember.getId(), source.getId(), target.getId()));
        return FavoriteResponse.of(persistFavorite, StationResponse.of(source), StationResponse.of(target));
    }

    public FavoriteResponse findLineResponseById(final LoginMember loginMember, final Long id) {
        final Favorite favorite = favoriteRepository.findByIdAndMemberId(id, loginMember.getId())
                .orElseThrow(NotFoundException::new);
        final Station source = stationService.findStationById(favorite.getSourceStationId());
        final Station target = stationService.findStationById(favorite.getTargetStationId());
        return FavoriteResponse.of(favorite, StationResponse.of(source), StationResponse.of(target));
    }

    @Transactional
    public void deleteLineById(final LoginMember loginMember, final Long id) {
        validateAuth(loginMember, id);
        favoriteRepository.deleteByMemberId(loginMember.getId());
    }

    private void validateAuth(final LoginMember loginMember, final Long id) {
        if (!id.equals(loginMember.getId())) {
            throw new PermissionException();
        }
    }
}
