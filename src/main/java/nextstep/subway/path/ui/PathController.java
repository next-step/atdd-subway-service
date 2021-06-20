package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    @GetMapping("/paths")
    public ResponseEntity getPaths(@ModelAttribute PathRequest pathRequest) {
        return ResponseEntity.ok().build();
    }
}
