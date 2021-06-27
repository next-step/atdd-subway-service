package nextstep.subway.path;

import nextstep.subway.exception.InvalidPathSearchingException;
import nextstep.subway.path.domain.ShortestPath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShortestPathTest {

    @Test
    void validate() {
        assertThatThrownBy(() -> {
            ShortestPath shortestPath = new ShortestPath(null);
        }).isInstanceOf(InvalidPathSearchingException.class);
    }

}
