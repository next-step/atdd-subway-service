package nextstep.subway.path.application;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortPath(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException();
        }

        List<Line> lines = lineRepository.findAll();
        List<Station> stations = lines.stream()
                                      .map(Line::getStations)
                                      .flatMap(Collection::stream)
                                      .collect(toList());

        boolean hasSourceStation = stations.stream()
                                           .map(Station::getId)
                                           .anyMatch(id -> id.equals(sourceId));
        boolean hasTargetStation = stations.stream()
                                           .map(Station::getId)
                                           .anyMatch(id -> id.equals(targetId));

        if (!hasSourceStation || !hasTargetStation) {
            throw new IllegalArgumentException();
        }

        return PathResponse.of(Collections.emptyList(), 0);
    }
}
