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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
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

    public FavoriteResponse create(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(memberId);
        Station source = findStation(favoriteRequest.getSource());
        Station target = findStation(favoriteRequest.getTarget());

        Favorite savedFavorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.of(savedFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> list(Long id) {
        Member member = findMember(id);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long favoriteId) {
        favoriteRepository.findById(favoriteId).orElseThrow(() -> {
            throw new NoSuchFavoriteException("존재하지 않는 즐겨찾기입니다. 즐겨찾기ID: " + favoriteId);
        });

        favoriteRepository.deleteById(favoriteId);
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
