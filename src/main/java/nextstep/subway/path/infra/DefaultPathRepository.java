package nextstep.subway.path.infra;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathRepository;
import nextstep.subway.path.domain.PathSection;
import nextstep.subway.path.domain.PathSections;
import nextstep.subway.path.domain.PathStation;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Repository
public class DefaultPathRepository implements PathRepository {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    @Override
    public PathSections findAllSections() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .flatMap(line -> line.getSections().stream())
                .map(this::toPathSection)
                .collect(collectingAndThen(toList(), PathSections::new));
    }

    private PathSection toPathSection(final Section section) {
        return new PathSection(section.getLine().getId(),
                toPathStation(section.getUpStation()),
                toPathStation(section.getDownStation()),
                section.getDistance());
    }

    private PathStation toPathStation(final Station station) {
        return new PathStation(station.getId(), station.getName(), station.getCreatedDate());
    }

    @Override
    public Optional<PathStation> findById(final Long id) {
        return stationRepository.findById(id)
                .map(this::toPathStation);
    }
}
