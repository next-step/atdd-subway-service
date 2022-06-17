package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.DeletedState;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(EntityNotFoundException::new);
        final Station destination = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(EntityNotFoundException::new);
        final Favorite savedFavorite = favoriteRepository.save(new Favorite(loginMember.getId(), source, destination));
        return new FavoriteResponse(savedFavorite.getId(),
                StationResponse.of(savedFavorite.getSource()),
                StationResponse.of(savedFavorite.getDestination()));
    }

    public List<FavoriteResponse> findFavorite(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findFavoritesByLoginMemberId(loginMember.getId());
        return favorites.stream()
                .map(favorite -> new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSource()), StationResponse.of(favorite.getDestination())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(long id) {
        Favorite favorite = favoriteRepository.getById(id);
        favorite.updateDeletedStateBy(DeletedState.YES);
    }
}
