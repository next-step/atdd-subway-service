package nextstep.subway.line.ui;

import nextstep.subway.line.application.PathService;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<List<StationResponse>> getShortestPath(@RequestParam("source") String sourceStationId
                                                                ,@RequestParam("target") String targetStationId) {
        List<StationResponse> stations = pathService.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok(stations);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
