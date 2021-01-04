package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {

    private final PathRepository pathRepository;

    private final PathFinder pathFinder;

    public PathResponse findShortest(final Long sourceId, final Long targetId) {
        PathSections allSections = pathRepository.findAllSections();
        PathStation source = findById(sourceId);
        PathStation target = findById(targetId);
        Path shortest = pathFinder.findShortest(allSections, source, target);
        return PathResponse.of(shortest);
    }

    private PathStation findById(final Long targetId) {
        return pathRepository.findById(targetId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
