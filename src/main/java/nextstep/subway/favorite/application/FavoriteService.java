package nextstep.subway.favorite.application;

import java.util.List;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Favorite favorite = new Favorite(
            findMemberById(memberId),
            stationRepository.getOne(request.getSourceStationId()),
            stationRepository.getOne(request.getTargetStationId()));

        Favorite savedFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(savedFavorite);
    }

    public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {
        List<Favorite> Favorites = favoriteRepository
            .findByMemberId(findMemberById(memberId).getId());
        return FavoriteResponse.of(Favorites);
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long id) {
        validateRemovable(memberId, id);
        favoriteRepository.deleteById(id);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(AuthorizationException::new);
    }

    public void validateRemovable(Long memberId, Long favoriteId) {
        findFavoritesByMemberId(findMemberById(memberId).getId())
            .stream()
            .filter(favorite -> favorite.getId().equals(favoriteId))
            .findAny()
            .orElseThrow(AuthorizationException::new);
    }
}
