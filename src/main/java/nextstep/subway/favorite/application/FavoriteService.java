package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
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
@Transactional
public class FavoriteService {
    private static final String ERROR_MESSAGE_ID_NULL = "즐겨찾기 ID 값이 null 입니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTS = "존재하지 않는 즐겨찾기입니다.";
    private static final String ERROR_MESSAGE_NOT_MINE = "본인의 즐겨찾기 항목이 아닙니다.";
    private static final String ERROR_MESSAGE_DUPLICATION = "이미 존재하는 경로입니다.";

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(FavoriteRequest request, LoginMember loginMember) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());

        Favorite favorite = new Favorite(source, target, loginMember.getId());
        validateDuplicatedFavorite(favorite, loginMember);

        Favorite persistFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(persistFavorite);
    }

    private void validateDuplicatedFavorite(Favorite newFavorite, LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        favorites.stream()
                .filter(favorite -> favorite.isSameRoute(newFavorite))
                .anyMatch(favorite -> {
                    throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATION);
                });
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId, LoginMember loginMember) {
        validateForDelete(favoriteId, loginMember);

        favoriteRepository.deleteById(favoriteId);
    }

    private void validateForDelete(Long favoriteId, LoginMember loginMember) {
        validateFavoriteId(favoriteId);
        validateFavoriteForDelete(favoriteId, loginMember);
    }

    private void validateFavoriteId(Long favoriteId) {
        if (favoriteId == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ID_NULL);
        }
    }

    private void validateFavoriteForDelete(Long favoriteId, LoginMember loginMember) {
        Optional<Favorite> favorite = favoriteRepository.findById(favoriteId);
        validateFavoriteExists(favorite);
        validateFavoriteOwner(favorite.get(), loginMember);
    }

    private void validateFavoriteExists(Optional<Favorite> optional) {
        if (!optional.isPresent()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS);
        }
    }

    private void validateFavoriteOwner(Favorite favorite, LoginMember loginMember) {
        if (!favorite.isOwner(loginMember.getId())) {
            throw new AuthorizationException(ERROR_MESSAGE_NOT_MINE);
        }
    }
}
