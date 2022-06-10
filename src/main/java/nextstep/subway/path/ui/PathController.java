package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
public class PathController {

    @GetMapping
    public PathResponse findPath(Long source, Long target) {
        return null;
    }
}
