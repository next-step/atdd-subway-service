package nextstep.subway.path.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findRouteSearch(@RequestBody Map<String, String> params) {
        return ResponseEntity.ok().build();
    }
}
