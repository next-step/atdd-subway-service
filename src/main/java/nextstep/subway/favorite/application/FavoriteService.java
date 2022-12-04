package nextstep.subway.favorite.application;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteService {

    public LineResponse saveLine(LineRequest request) {
        return null;
    }
}
