package nextstep.subway.path.service;

import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    public List<StationResponse> findPaths(Long source, Long target) {
        return null;
    }
}
