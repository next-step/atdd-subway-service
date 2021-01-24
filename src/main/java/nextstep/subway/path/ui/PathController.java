package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService){
        this.pathService = pathService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> fPathResponseindRouteSearch(@RequestBody PathRequest params) {
        PathResponse response = pathService.findRouteSearch(params.getSource(), params.getTarget());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
}
