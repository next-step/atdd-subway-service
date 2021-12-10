package nextstep.subway.favorite.application;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.auth.domain.*;
import nextstep.subway.common.exception.*;
import nextstep.subway.favorite.domain.*;
import nextstep.subway.member.domain.*;
import nextstep.subway.station.domain.*;

@Service
@Transactional(readOnly = true)
public class FavoriteReadService {
    private static final String STATION = "역";

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteReadService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Station findStationById(final Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(STATION));
    }

    public List<Favorite> findFavorites(LoginMember loginMember) {
        return favoriteRepository.findFavoritesByMember(
            Member.of(loginMember.getId(), loginMember.getEmail(), loginMember.getAge())
        );
    }
}
