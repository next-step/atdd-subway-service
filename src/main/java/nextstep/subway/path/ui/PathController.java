package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/paths")
public class PathController {

    private final PathFinder pathFinder;

    public PathController(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    @GetMapping
    public ResponseEntity getPath(@ModelAttribute PathRequest pathRequest) {

        PathResponse pathResponse = pathFinder.getPath(pathRequest);

        return ResponseEntity.ok().body(pathResponse);
    }
}
