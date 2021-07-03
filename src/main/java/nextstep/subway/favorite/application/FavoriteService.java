package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long id, FavoriteRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("유효하지 않은 회원입니다."));
        Station source = stationRepository.findById(request.getSourceId()).orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다."));
        Station target = stationRepository.findById(request.getTargetId()).orElseThrow(() -> new IllegalArgumentException("도착역이 존재하지 않습니다."));

        Favorite savedFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(savedFavorite);
    }

    public List<FavoriteResponse> findAll(Long id) {
        List<Favorite> favorites = favoriteRepository.findByMemberId(id).orElseThrow(() -> new IllegalArgumentException("즐겨찾기가 없습니다."));
        return getFavoriteResponses(favorites);
    }

    private List<FavoriteResponse> getFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }


    public void deleteFavorite(Long memberId, Long favoriteId) {

    }
}
