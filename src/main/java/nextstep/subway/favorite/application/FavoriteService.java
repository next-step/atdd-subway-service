package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);
        Favorite persistFavorite = favoriteRepository.save(favoriteRequest.toFavorite(loginMember.getId(), source, target));
        return FavoriteResponse.of(persistFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> persistFavorites = favoriteRepository.findAll();
        return persistFavorites.stream()
                .map(favorite -> FavoriteResponse.of(favorite))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
