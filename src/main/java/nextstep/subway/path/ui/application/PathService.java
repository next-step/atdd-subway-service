package nextstep.subway.path.ui.application;

import nextstep.subway.path.ui.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : nextstep.subway.path.ui.application
 * fileName : PathService
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@Service
@Transactional(readOnly = true)
public class PathService {
    private PathFinder finder;

    public PathService(PathFinder finder) {
        this.finder = finder;
    }

    public PathResponse getPath(Long source, Long target) {
        return finder.getPath();
    }
}
