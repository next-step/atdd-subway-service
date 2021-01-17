package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.application.NoSuchStationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository,
                           StationRepository stationRepository,
                           FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse create(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(memberId);
        Station source = findStation(favoriteRequest.getSource());
        Station target = findStation(favoriteRequest.getTarget());

        Favorite savedFavorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(savedFavorite);
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> {
                    throw new NoSuchStationException("존재하지 않는 역입니다. 역ID: " + stationId);
        });
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new NoSuchMemberException("존재하지 않는 회원입니다. 회원ID: " + memberId);
        });
    }

}
