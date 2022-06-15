package nextstep.subway.favorite.service;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;


    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite saveFavorite(Member member, Station sourceStation, Station destStation) {
        Favorite favorite = new Favorite(sourceStation, destStation, member);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> getFavoriteList(Member member) {
        return favoriteRepository.findAllByMember(member)
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, Member member) {
        Favorite favorite = favoriteRepository.findByMemberAndId(member, id)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 즐겨찾기를 찾을수 없습니다"));
        favoriteRepository.delete(favorite);
    }
}
