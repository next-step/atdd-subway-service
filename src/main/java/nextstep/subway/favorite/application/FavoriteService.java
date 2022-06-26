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

    public FavoriteService(final MemberRepository memberRepository, final StationRepository stationRepository, final FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse addFavorite(final Long memberId, final FavoriteRequest favoriteRequest) {
        final Member member = findByMember(memberId);
        final Station sourceStation = findByStation(favoriteRequest.getSource());
        final Station targetStation = findByStation(favoriteRequest.getTarget());

        final Favorite favorite = favoriteRepository.save(Favorite.of(member, sourceStation, targetStation));

        return FavoriteResponse.of(favorite.getId(), sourceStation, targetStation);
    }

    @Transactional(readOnly = true)
    public Member findByMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Station findByStation(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(RuntimeException::new);
    }

}
