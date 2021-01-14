package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteDeleteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(FavoriteCreateRequest request) {
        Member member = memberRepository.findById(request.getUserId()).orElseThrow(IllegalArgumentException::new);
        Station source = stationService.findStationById(request.getSourceStationId());
        Station target = stationService.findStationById(request.getTargetStationId());
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

    public void deleteFavorite(FavoriteDeleteRequest request) {
        Member member = memberRepository.findById(request.getUserId()).orElseThrow(IllegalArgumentException::new);
        Favorite favorite = favoriteRepository.findById(request.getFavoriteId()).orElseThrow(IllegalArgumentException::new);
        member.removeFavorite(favorite);
        favoriteRepository.deleteById(request.getFavoriteId());
    }
}
