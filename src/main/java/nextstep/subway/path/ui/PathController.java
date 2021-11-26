package nextstep.subway.path.ui;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> removeLineStation(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse;

        try {
            pathResponse = pathService.searchShortestPath(source, target);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(pathResponse);
    } 
}
