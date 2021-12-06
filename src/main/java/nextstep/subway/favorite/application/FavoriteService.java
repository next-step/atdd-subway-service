package nextstep.subway.favorite.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Member member = memberService.findById(memberId);
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        Favorite favorite = favoriteRepository.save(request.toFavorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findFavoritesById(memberId);
        return convertToFavoriteResponses(favorites);
    }

    private List<FavoriteResponse> convertToFavoriteResponses(List<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        for (Favorite favorite : favorites) {
            favoriteResponses.add(FavoriteResponse.of(favorite));
        }
        return favoriteResponses;
    }
}
