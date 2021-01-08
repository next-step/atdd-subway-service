package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberRepository memberRepository, final StationRepository stationRepository, final FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Long createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
        final Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(NotFoundException::new);
        final Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(NotFoundException::new);
        final Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(NotFoundException::new);

        final Favorite savedFavorite = favoriteRepository.save(Favorite.of(member, source, target));

        return savedFavorite.getId();
    }
}
