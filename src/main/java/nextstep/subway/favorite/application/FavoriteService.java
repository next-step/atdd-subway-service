package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.error.ErrorCode.*;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> findAllBy(LoginMember loginMember) {
        return favoriteRepository.findByMemberId(loginMember.getId()).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource()).orElseThrow(() -> new ErrorCodeException(NO_EXISTS_STATION));
        Station target = stationRepository.findById(request.getTarget()).orElseThrow(() -> new ErrorCodeException(NO_EXISTS_STATION));
        Favorite favorite = new Favorite(loginMember.getId(), source, target);
        favorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    public void deleteById(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new ErrorCodeException(CANNOT_FOUND_FAVORITE));
        if (!favorite.isCreateBy(loginMember.getId())) {
            throw new ErrorCodeException(CANNOT_DELETE_FAVORITE);
        }
        favoriteRepository.delete(favorite);
    }
}
