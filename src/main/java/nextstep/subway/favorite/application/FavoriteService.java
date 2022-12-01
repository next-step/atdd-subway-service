package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, LineRepository lineRepository,
            FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = findMemberById(memberId);
        Station sourceStation = findStationById(favoriteRequest.getSource());
        Station targetStation = findStationById(favoriteRequest.getTarget());
        validateFindPath(sourceStation, targetStation);

        Favorite favorite = new Favorite.Builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .build();
        return FavoriteResponse.from(favoriteRepository.save(favorite));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(AuthorizationException::new);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(SubwayException::new);
    }

    private void validateFindPath(Station sourceStation, Station targetStation) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = DijkstraShortestPathFinder.from(lines);
        Path path = pathFinder.findPath(sourceStation, targetStation);
        Optional.ofNullable(path).orElseThrow(SubwayException::new);
    }
}
