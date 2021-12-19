package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoritesResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        validateFavorite(favoriteRequest);
        Member member = findMemberById(memberId);
        Station sourceStation = findStationById(favoriteRequest.getSource());
        Station targetStation = findStationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return favoriteToResponse(favorite);
    }

    public FavoritesResponse findFavorites(Long memberId) {
        Member member = findMemberById(memberId);
        List<Favorite> favorites = member.getFavorites();
        return favoritesToFavoritesResponse(favorites);
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new EntityNotFoundException("즐겨찾기를 찾을 수 없습니다."));
        favoriteRepository.delete(favorite);
    }

    private void validateFavorite(FavoriteRequest favoriteRequest) {
        if (favoriteRequest.getSource().equals(favoriteRequest.getTarget())) {
            throw new IllegalArgumentException("즐겨찾기 등록에 출발역과 도착역이 같으면 안됩니다.");
        }
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new EntityNotFoundException("역을 찾을 수 없습니다."));
    }

    private FavoriteResponse favoriteToResponse(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSource()), StationResponse.of(favorite.getTarget()));
    }

    private FavoritesResponse favoritesToFavoritesResponse(List<Favorite> favorites) {
        return new FavoritesResponse(
                favorites.stream()
                        .map(this::favoriteToResponse)
                        .collect(Collectors.toList())
        );
    }
}
