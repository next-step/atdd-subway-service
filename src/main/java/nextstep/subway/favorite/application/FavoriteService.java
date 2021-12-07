package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private static final String STATION = "역"; 
    private static final String FAVORITE = "즐겨찾기";

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Favorite saveFavorite(LoginMember member, FavoriteRequest favoriteRequest) {
        return favoriteRepository.save(
            Favorite.of(
                Member.of(member.getId(), member.getEmail(), member.getAge()),
                findStationById(favoriteRequest.getSource()),
                findStationById(favoriteRequest.getTarget())
            )
        );
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(STATION));
    }

    @Transactional(readOnly = true)
    public List<Favorite> findFavorites(LoginMember loginMember) {
        return favoriteRepository.findFavoritesByMember(
            Member.of(loginMember.getId(), loginMember.getEmail(), loginMember.getAge())
        );
    }

    public void deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(FAVORITE));
        favoriteRepository.delete(favorite);
    }
}
