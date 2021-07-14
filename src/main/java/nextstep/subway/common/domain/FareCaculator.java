package nextstep.subway.common.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.StationGraphPath;

import java.util.List;

public interface FareCaculator {
    SubwayFare calculate(List<Section> allSectionList, StationGraphPath stationGraphPath, Integer age);
}
