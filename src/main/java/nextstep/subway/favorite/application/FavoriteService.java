package nextstep.subway.favorite.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.repository.FavoriteRepository;
import nextstep.subway.line.repository.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           MemberRepository memberRepository,
                           StationRepository stationRepository,
                           LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(memberId);
        Station sourceStation = findStation(favoriteRequest.getSource());
        Station targetStation = findStation(favoriteRequest.getTarget());

        checkedLinkedStation(sourceStation, targetStation);

        Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = findMember(memberId);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavoriteById(Long memberId, Long favoriteId) {
        Member member = findMember(memberId);
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, member.getId()).orElseThrow(
                () -> new EntityNotFoundException("Favorite", favoriteId)
        );
        favoriteRepository.delete(favorite);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new EntityNotFoundException("Member", memberId)
        );
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(
                () -> new EntityNotFoundException("Station", stationId)
        );
    }

    private void checkedLinkedStation(Station sourceStation, Station targetStation) {
        PathFinder pathFinder = new PathFinder();
        pathFinder.findFastPaths(lineRepository.findAll(), sourceStation, targetStation);
    }

}
