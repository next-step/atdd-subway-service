package nextstep.subway.favorite.application;

import nextstep.subway.exception.NoSearchFavoriteException;
import nextstep.subway.exception.NoSearchMemberException;
import nextstep.subway.exception.NoSearchStationException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository,
                           StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member persistMember = findMemberById(loginMemberId);
        Station source = findStationById(favoriteRequest.getSourceId());
        Station target = findStationById(favoriteRequest.getTargetId());
        Favorite persistFavorite = favoriteRepository.save(Favorite.of(persistMember, source, target));
        return FavoriteResponse.of(persistFavorite);
    }

    private Member findMemberById(Long loginMemberId) {
        return memberRepository.findById(loginMemberId).orElseThrow(() -> new NoSearchMemberException(loginMemberId));
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new NoSearchStationException(stationId));
    }

    public List<FavoriteResponse> getAllFavorites(Long loginMemberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMemberId);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long loginMemberId, Long favoriteId) {
        Favorite favorite = findFavoriteById(favoriteId);
        favorite.vlidateOwner(loginMemberId);
        favoriteRepository.deleteById(favoriteId);
    }

    private Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(() -> new NoSearchFavoriteException(favoriteId));
    }
}
