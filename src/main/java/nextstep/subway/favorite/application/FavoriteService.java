package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.exceptions.FavoriteEntityNotFoundException;
import nextstep.subway.favorite.application.exceptions.NotMyFavoriteException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.adapters.SafeStationForFavoriteAdapter;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import nextstep.subway.favorite.ui.dto.FavoriteResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteFavorite(LoginMember loginMember, Long deleteTarget) {
        Favorite favorite = this.getFavoriteById(deleteTarget);
        validateIsOwnerOfFavorite(loginMember, favorite);

        favoriteRepository.deleteById(deleteTarget);
    }

    public Long saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        try {
            validateStationsExists(favoriteRequest);

            Favorite saved = favoriteRepository.save(
                    new Favorite(loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget()));

            return saved.getId();
        } catch (DataIntegrityViolationException e) {
            throw new FavoriteCreationException("즐겨찾기를 중복해서 등록할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Favorite getFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteEntityNotFoundException("존재하지 않는 즐겨찾기 입니다."));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());

        return favorites.stream().map(it -> FavoriteResponse.of(
                it.getId(),
                safeStationAdapter.getSafeStationInFavorite(it.getSourceId()),
                safeStationAdapter.getSafeStationInFavorite(it.getTargetId()))
        ).collect(Collectors.toList());
    }

    private void validateIsOwnerOfFavorite(final LoginMember loginMember, final Favorite favorite) {
        if (!favorite.isOwner(loginMember.getId())) {
            throw new NotMyFavoriteException("본인의 즐겨찾기만 삭제 가능합니다.");
        }
    }

    private void validateStationsExists(final FavoriteRequest favoriteRequest) {
        if(!safeStationAdapter.isAllExists(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
            throw new FavoriteCreationException("존재하지 않는 역으로 즐겨찾기를 생성할 수 없습니다.");
        }
    }
}
