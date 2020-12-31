package nextstep.subway.path.domain.adapters;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SafeSectionInfo;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SafeLineAdapter implements SafeLine {
    private final LineService lineService;

    public SafeLineAdapter(LineService lineService) {
        this.lineService = lineService;
    }

    @Override
    public List<Long> getAllStationIds() {
        List<Line> lines = lineService.findAllLines();

        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<SafeSectionInfo> getAllSafeSectionInfos() {
        List<Line> lines = lineService.findAllLines();

        return lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new SafeSectionInfo(
                        it.getUpStation().getId(), it.getDownStation().getId(), it.getDistance().value()))
                .collect(Collectors.toList());
    }
}
