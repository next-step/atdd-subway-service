package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {
    private static final String NOT_OWNED_FAVORITE_ERR_MSG = "즐겨찾기에 대한 권한이 없습니다.";
    private static final String NOT_FOUND_FAVORITE_ERR_MSG = "즐겨찾기를 찾을 수 없습니다.";

    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(final Long memberId, final FavoriteRequest favoriteRequest) {
        final Station source = stationService.findById(favoriteRequest.getSource());
        final Station target = stationService.findById(favoriteRequest.getTarget());

        final Favorite favorite = new Favorite(source, target, memberId);
        favoriteRepository.save(favorite);

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findAllFavoritesByMemberId(final Long memberId) {
        final List<Favorite> favorites = favoriteRepository.findAllByOwnerId(memberId);
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavoriteById(final Long memberId, Long id) {
        final Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_FAVORITE_ERR_MSG));
        if (!favorite.isOwnedBy(memberId)) {
            throw new BadRequestException(NOT_OWNED_FAVORITE_ERR_MSG);
        }

        favoriteRepository.deleteById(id);
    }
}
