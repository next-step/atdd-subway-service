package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
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

    private final FavoriteRepository favoriteRepository;

    private final MemberRepository memberRepository;

    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());

        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);

        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
