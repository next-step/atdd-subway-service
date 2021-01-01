package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.exceptions.FavoriteEntityNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.adapters.SafeStationForFavoriteAdapter;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import nextstep.subway.favorite.ui.dto.FavoriteResponse;
import nextstep.subway.favorite.ui.dto.StationInFavoriteResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SafeStationForFavoriteAdapter safeStationAdapter;

    public FavoriteService(FavoriteRepository favoriteRepository, SafeStationForFavoriteAdapter safeStationAdapter) {
        this.favoriteRepository = favoriteRepository;
        this.safeStationAdapter = safeStationAdapter;
    }

    public void deleteFavorite(final Long deleteTarget) {
        try {
            favoriteRepository.deleteById(deleteTarget);
        } catch (EmptyResultDataAccessException e) {
            throw new FavoriteEntityNotFoundException("존재하지 않는 즐겨찾기입니다.");
        }
    }

    public Long saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        validateStationsExists(favoriteRequest);

        Favorite saved = favoriteRepository.save(
                new Favorite(loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget()));

        return saved.getId();
    }

    private void validateStationsExists(final FavoriteRequest favoriteRequest) {
        if(!safeStationAdapter.isAllExists(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
            throw new FavoriteCreationException("존재하지 않는 역으로 즐겨찾기를 생성할 수 없습니다.");
        }
    }

    public List<FavoriteResponse> getFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());

        return favorites.stream().map(it -> FavoriteResponse.of(
                it.getId(),
                safeStationAdapter.getSafeStationInFavorite(it.getSourceId()),
                safeStationAdapter.getSafeStationInFavorite(it.getTargetId()))
        ).collect(Collectors.toList());
    }
}
