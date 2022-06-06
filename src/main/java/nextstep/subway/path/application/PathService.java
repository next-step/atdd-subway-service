package nextstep.subway.path.application;

import com.google.common.collect.Lists;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    public PathResponse findShortPath(Long source, Long target) {
        return PathResponse.of(Lists.newArrayList(), 1);
    }
}
