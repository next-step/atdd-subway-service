package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.exception.NotFoundException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findShortestPath(final long source, final long target) {
        final PathFinder pathFinder = new PathFinder(sectionRepository.findAll());
        final Station sourceStation = validStation(source);
        final Station targetStation = validStation(target);
        final Path path = pathFinder.findShortestPath(sourceStation, targetStation);

        return PathResponse.of(path);
    }

    private Station validStation(final long id) {
        return stationRepository.findById(id)
            .orElseThrow(NotFoundException::new);
    }
}
