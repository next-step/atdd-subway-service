package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final LineService lineService;
    private final StationService stationService;

    public PathController(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        Map<Long, Station> stations = stationService.findByIds(asList(sourceId, targetId))
                .stream()
                .collect(toMap(Station::getId, Function.identity()));
        PathFinder pathFinder = new PathFinder(lineService.findAllLines());
        return ResponseEntity.ok(pathFinder.findPath(stations.get(sourceId), stations.get(targetId)));
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity<String> handleIllegalException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
