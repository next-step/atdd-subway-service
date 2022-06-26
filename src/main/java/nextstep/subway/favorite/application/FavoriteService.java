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

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationRepository stationRepository,
                           MemberRepository memberRepository) {

        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(long memberId, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.getById(memberId);
        Station source = stationRepository.getById(favoriteRequest.getSource());
        Station target = stationRepository.getById(favoriteRequest.getTarget());

        final Favorite save = favoriteRepository.save(new Favorite(member, source, target));

        return FavoriteResponse.of(save);
    }
}
