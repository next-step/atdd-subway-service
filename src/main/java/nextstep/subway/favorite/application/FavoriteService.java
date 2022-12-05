package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.Favorites;
import nextstep.subway.favorite.dto.FavoriteCreatedRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse create(Long memberId, FavoriteCreatedRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Station source = stationRepository.findById(request.getSourceId()).orElseThrow(EntityNotFoundException::new);
        Station target = stationRepository.findById(request.getTargetId()).orElseThrow(EntityNotFoundException::new);
        Favorite favorite = new Favorite(member, source, target);
        validateDuplicate(member, favorite);
        return new FavoriteResponse(favoriteRepository.save(favorite));
    }

    private void validateDuplicate(Member member, Favorite favorite) {
        Favorites favorites = new Favorites();
        favorites.addAll(favoriteRepository.findAllByMember(member));
        favorites.add(favorite);
    }

    public List<FavoriteResponse> findAll(Member member) {
        return favoriteRepository.findAllByMember(member).stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    public void delete(Member member, Long favoriteId) {
        Favorites favorites = new Favorites();
        favorites.addAll(favoriteRepository.findAllByMember(member));
        Favorite deleteFavorite = favoriteRepository.findById(favoriteId).orElseThrow(EntityNotFoundException::new);
        favorites.validateDeleteFavorite(deleteFavorite);
        favoriteRepository.deleteById(favoriteId);
    }
}
