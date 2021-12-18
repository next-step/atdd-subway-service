package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.HasNotPermissionException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Favorite saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Favorite save = favoriteRepository.save(new Favorite(loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget()));
        return save;
    }

    public List<FavoriteResponse> findFavorite(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(loginMember.getId());
        return favorites.stream()
                .map(favorite -> FavoriteResponse.of(favorite.getMemberId(),
                        new StationResponse(stationRepository.findById(favorite.getSourceStationId()).orElseThrow(IllegalArgumentException::new)),
                        new StationResponse(stationRepository.findById(favorite.getTargetStationId()).orElseThrow(IllegalArgumentException::new)))
                ).collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(IllegalArgumentException::new);
        if(!favorite.isCreatedBy(loginMember.getId())) {
            throw new HasNotPermissionException(loginMember.getId() + "는 삭제할 권한이 없습니다.");
        }
        favoriteRepository.delete(favorite);
    }
}
