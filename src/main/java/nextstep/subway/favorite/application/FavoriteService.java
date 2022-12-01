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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(
        MemberRepository memberRepository,
        StationRepository stationRepository,
        FavoriteRepository favoriteRepository
    ) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMemberId).orElseThrow(RuntimeException::new);
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(RuntimeException::new);
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(RuntimeException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
        return FavoriteResponse.from(favorite);
    }
}
