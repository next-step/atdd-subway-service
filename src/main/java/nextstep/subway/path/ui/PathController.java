package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static nextstep.subway.PageController.URIMapping.PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(PATH)
public class PathController {
    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> findLineById(PathRequest request) {
        return ResponseEntity.ok(pathService.searchPath(request));
    }
}
