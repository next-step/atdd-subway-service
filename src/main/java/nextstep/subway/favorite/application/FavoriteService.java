package nextstep.subway.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member persistMember = findMemberById(loginMemberId);
        Station source = findStationById(favoriteRequest.getSourceId());
        Station target = findStationById(favoriteRequest.getTargetId());
        Favorite persistFavorite = favoriteRepository.save(new Favorite(persistMember, source, target));

        return FavoriteResponse.from(persistFavorite);
    }

    private Member findMemberById(Long loginMemberId) {
        return memberRepository.findById(loginMemberId).orElseThrow(NoSuchElementException::new);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(NoSuchElementException::new);
    }

    public List<FavoriteResponse> findFavorites(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMemberId);
        return favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        Favorite favorite = findFavorite(favoriteId);
        favoriteRepository.delete(favorite);
    }

    private Favorite findFavorite(Long favoriteId) {
        return favoriteRepository.findById(favoriteId)
            .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteFavoritesByMemberId(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMemberId);
        favoriteRepository.deleteAll(favorites);
    }
}
