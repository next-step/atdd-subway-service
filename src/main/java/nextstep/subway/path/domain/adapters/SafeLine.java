package nextstep.subway.path.domain.adapters;

import nextstep.subway.path.domain.SafeSectionInfo;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;

import java.util.List;

public interface SafeLine {
    List<Long> getAllStationIds();
    List<SafeSectionInfo> getAllSafeSectionInfos();
    LineOfStationInPaths getLineOfStationInPaths(List<Long> stationIds);
}
