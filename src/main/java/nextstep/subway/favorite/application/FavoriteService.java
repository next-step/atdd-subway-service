package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
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
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            StationRepository stationRepository,
            MemberRepository memberRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(memberId);
        Station source = findStation(favoriteRequest.getSource());
        Station target = findStation(favoriteRequest.getTarget());
        Favorite favorite = new Favorite(member, source, target);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.from(savedFavorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private Station findStation(Long favoriteRequest) {
        return stationRepository.findById(favoriteRequest).orElseThrow(EntityNotFoundException::new);
    }
}
