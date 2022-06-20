package nextstep.subway.favorite.application;

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

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoriteService(final FavoriteRepository favoriteRepository, final StationRepository stationRepository,
                           final MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(RuntimeException::new);
        final Station source = stationRepository.findById(favoriteRequest.getSource())
                .orElseThrow(RuntimeException::new);
        final Station target = stationRepository.findById(favoriteRequest.getTarget())
                .orElseThrow(RuntimeException::new);
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(favorite);
    }
}
