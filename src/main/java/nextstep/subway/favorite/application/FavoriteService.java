package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Member member, Station sourceStation, Station targetStation) {
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findMyFavorites(LoginMember loginMember) {
        return FavoriteResponse.of(favoriteRepository.findAllByMemberId(loginMember.getId()));
    }

    @Transactional
    public void removeFavorite(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(NoSuchElementException::new);
        favorite.isOwnedBy(loginMember.getId());
        favoriteRepository.delete(favorite);
    }
}
