package nextstep.subway.path.domain.line;

import nextstep.subway.line.domain.Line;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LinePolicyFixture {
    public static final List<Line> 환승_최대요금_7000 = Collections.unmodifiableList(
            Arrays.asList(
                new Line("1호선", "1호선", 1000),
                new Line("2호선", "2호선", 2000),
                new Line("3호선", "3호선", 3000),
                new Line("4호선", "4호선", 500),
                new Line("5호선", "5호선", 7000)
            )
    );

    public static final List<Line> 비환승_요금_3000 = Collections.unmodifiableList(
            Arrays.asList(
                    new Line("3호선", "3호선", 3000)
            )
    );
}
