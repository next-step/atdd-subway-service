package nextstep.subway.favorite.service;

import java.util.List;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;


    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> findAllByMember(Member member) {
        return favoriteRepository.findAllByMember(member);
    }

    public Favorite findByMemberAndId(Member member, Long id) {
        return favoriteRepository.findByMemberAndId(member, id)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 즐겨찾기를 찾을수 없습니다"));
    }

    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}
