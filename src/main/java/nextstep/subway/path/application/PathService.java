package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    public PathResponse getShortestPaths(Long source, Long target) {
        System.out.println(source);
        System.out.println(target);

        return new PathResponse();
    }
}
