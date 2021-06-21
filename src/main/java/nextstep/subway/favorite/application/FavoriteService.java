package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long myId, FavoriteRequest favoriteRequest) {
        Member me = memberRepository.findById(myId).orElseThrow(NoSuchElementException::new);
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(NoSuchElementException::new);
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(NoSuchElementException::new);

        Favorite persistFavorite = favoriteRepository.save(new Favorite(me, source, target));

        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(Long myId) {
        Member me = memberRepository.findById(myId).orElseThrow(NoSuchElementException::new);

        List<Favorite> favorites = favoriteRepository.findByMember(me);

        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void deleteFavorite(Long myId, Long id) {
        Member me = memberRepository.findById(myId).orElseThrow(NoSuchElementException::new);

        List<Favorite> favorites = favoriteRepository.findByMember(me);

        favorites.stream().filter(favorite -> favorite.getId().equals(id))
                .findAny().orElseThrow(AuthorizationException::new);

        favoriteRepository.deleteById(id);
    }
}
