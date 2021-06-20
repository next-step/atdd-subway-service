package nextstep.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    @GetMapping(value = "/paths")
    ResponseEntity getPaths(@RequestParam(value = "source") int source,
                            @RequestParam(value = "target") int target) {
        return null;


    }

}
