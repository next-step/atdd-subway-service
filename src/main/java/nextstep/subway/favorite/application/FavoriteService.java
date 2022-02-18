package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long id, FavoriteRequest request) {
        Member member = member(id);
        Station source = station(request.getSource());
        Station target = station(request.getTarget());
        Favorite favorite = favoriteRepository.save(request.toFavorite(member, source, target));
        return FavoriteResponse.of(favorite, StationResponse.of(source), StationResponse.of(target));
    }

    private Member member(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    private Station station(Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(Long id) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(id);
        return favorites.stream()
            .map(favorite -> FavoriteResponse.of(
                favorite,
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())))
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
