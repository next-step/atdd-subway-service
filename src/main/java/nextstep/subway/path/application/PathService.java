package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.Money;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.infra.JgraphtPathFinder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {

    private final PathRepository pathRepository;

    private final PathFinder pathFinder = JgraphtPathFinder.getInstance();

    private final DistanceFee distanceFee;

    public PathResponse findShortest(final long sourceId, final long targetId) {
        PathSections allSections = pathRepository.findAllSections();
        PathStation source = findById(sourceId);
        PathStation target = findById(targetId);
        Path shortest = pathFinder.findShortest(allSections, source, target);
        return PathResponse.of(shortest);
    }

    public PathResponse findShortestWithFee(final long sourceId, final long targetId) {
        PathSections allSections = pathRepository.findAllSections();
        PathStation source = findById(sourceId);
        PathStation target = findById(targetId);
        Path shortest = pathFinder.findShortest(allSections, source, target);
        Money distanceFee = shortest.settle(this.distanceFee);
        return PathResponse.of(shortest, distanceFee);
    }

    private PathStation findById(final long targetId) {
        return pathRepository.findById(targetId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
