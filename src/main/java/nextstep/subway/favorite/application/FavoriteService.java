package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(Member member, FavoriteRequest request) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        return FavoriteResponse.of(favoriteRepository.save(new Favorite(member, source, target)));
    }

    public List<FavoriteResponse> findAllFavorites(Member member) {
        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void removeFavorite(Member member, Long favoriteId) {
        Favorite favorite = findById(favoriteId);
        if(!favorite.getMember().equals(member)) {
            throw new IllegalStateException("삭제하고자하는 Favorite id:" + favoriteId + "는 Member id:" + member.getId() + "의 소유가 아닙니다.");
        }
        favoriteRepository.delete(favorite);
    }

    public Favorite findById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new NoSuchElementException("Favorite id:" + favoriteId + " 존재하지않습니다."));
    }
}
