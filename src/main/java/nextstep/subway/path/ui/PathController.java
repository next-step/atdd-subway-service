package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = new PathResponse();
        pathResponse.add(new StationResponse(3L, "교대역", null, null));
        pathResponse.add(new StationResponse(4L, "남부터미널역", null, null));
        pathResponse.add(new StationResponse(2L, "양재역", null, null));
        return ResponseEntity.ok(pathResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
}
