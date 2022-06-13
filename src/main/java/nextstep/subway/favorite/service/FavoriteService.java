package nextstep.subway.favorite.service;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public Favorite saveFavorite(Member member, FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());
        Favorite favorite = new Favorite(sourceStation, targetStation, member);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> getFavoriteList(Member member) {
        return favoriteRepository.findAllByMember(member)
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id){
        favoriteRepository.deleteById(id);
    }
}
