package nextstep.subway.path.ui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : nextstep.subway.path.ui.controller
 * fileName : PathController
 * author : haedoang
 * date : 2021/12/04
 * description : 경로 컨트롤러
 */

@RestController
@RequestMapping("paths")
public class PathController {

    @GetMapping
    public ResponseEntity getShortestPath(@RequestParam(name = "source") Long source, @RequestParam(name = "target") Long target) {
        return ResponseEntity.ok().build();
    }
}
