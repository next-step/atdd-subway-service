package nextstep.subway.path.domain.fixture;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.fixture.LineFixture;
import nextstep.subway.line.domain.fixture.SectionFixture;
import nextstep.subway.path.domain.SubwayGraph;
import org.assertj.core.util.Lists;

import java.util.List;

public class SubwayGraphFixture {
    public static final Line 신분당선 = 신분당선_생성();
    public static final Line 삼호선 = 삼호선_생성();
    public static final SubwayGraph 지하철_노선도 = 지하철_노선도_생성(신분당선, 삼호선);

    public static Line 신분당선_생성() {
        Line 신분당선 = LineFixture.신분당선;
        지하철_노선에_구역을_등록(신분당선, Lists.newArrayList(
                SectionFixture.강남역_양재역,
                SectionFixture.양재역_양재시민의숲역,
                SectionFixture.양재시민의숲역_청계산입구역,
                SectionFixture.청계산입구역_판교역,
                SectionFixture.판교역_정자역,
                SectionFixture.정자역_미금역,
                SectionFixture.미금역_동천역,
                SectionFixture.동천역_광교역
        ));

        return 신분당선;
    }

    public static Line 삼호선_생성() {
        Line 삼호선 = LineFixture.삼호선;
        지하철_노선에_구역을_등록(삼호선, Lists.newArrayList(
                SectionFixture.교대역_남부터미널역,
                SectionFixture.남부터미널역_양재역,
                SectionFixture.양재역_매봉역,
                SectionFixture.매봉역_도곡역,
                SectionFixture.도곡역_대치역
        ));

        return 삼호선;
    }

    public static SubwayGraph 지하철_노선도_생성(Line... lines) {
        return SubwayGraph.of(Lists.newArrayList(lines));
    }

    public static void 지하철_노선에_구역을_등록(Line line, List<Section> sections) {
        for (Section section : sections) {
            line.getSections().add(section);
        }
    }
}
