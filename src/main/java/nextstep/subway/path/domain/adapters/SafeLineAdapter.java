package nextstep.subway.path.domain.adapters;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SafeSectionInfo;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPath;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;
import nextstep.subway.path.domain.fee.transferFee.LineWithExtraFee;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;

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

    @Override
    public LineOfStationInPaths getLineOfStationInPaths(List<Long> stationIds) {
        List<Line> lines = lineService.findAllLines();

        return stationIds.stream()
                .map(it -> parseLineToLineOfStationInPath(lines, it))
                .collect(collectingAndThen(Collectors.toList(), LineOfStationInPaths::new));
    }

    private LineOfStationInPath parseLineToLineOfStationInPath(List<Line> lines, Long stationId) {
        return lines.stream()
                .filter(line -> line.isBelongedStation(stationId))
                .map(belongedLine -> new LineWithExtraFee(belongedLine.getId(), belongedLine.getExtraFee().getValue()))
                .collect(collectingAndThen(Collectors.toList(), LineOfStationInPath::new));
    }
}
