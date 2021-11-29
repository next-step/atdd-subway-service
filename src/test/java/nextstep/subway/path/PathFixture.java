package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.Arrays;

public class PathFixture {

    public static final Line 이호선 = new Line("이호선", "red");
    public static final Station 교대역 = new Station("교대역");
    public static final Station 양재역 = new Station("양재역");
    public static final Station 선릉역 = new Station("선릉역");
    public static final Sections 구간 = new Sections(Arrays.asList(
            new Section(이호선, 교대역, 선릉역, 10),
            new Section(이호선, 선릉역, 양재역, 10)));
}
