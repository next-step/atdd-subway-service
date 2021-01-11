package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long memberId, Long sourceStationId, Long targetStationId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        Station source = stationRepository.findById(sourceStationId).orElseThrow(IllegalArgumentException::new);
        Station target = stationRepository.findById(targetStationId).orElseThrow(IllegalArgumentException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorite(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        List<Favorite> favorites = member.getFavorites();
        return favorites.stream()
                .map(favorite -> FavoriteResponse.of(favorite))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(IllegalArgumentException::new);
        member.removeFavorite(favorite);
    }
}
