package nextstep.subway.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberNotExistException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Long sourceId = favoriteRequest.getSourceId();
        Long targetId = favoriteRequest.getTargetId();

        Member member = findMemberById(memberId);
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);

        Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
        return FavoriteResponse.from(favorite);
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new MemberNotExistException("즐겨찾기 요청 대상자를 찾을 수 없습니다."));
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("정류장이 존재하지 않습니다."));
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        return favoriteRepository.findByMemberId(memberId)
            .stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());
    }
}
