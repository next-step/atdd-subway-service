package nextstep.subway.path.domain.adapters;

import nextstep.subway.path.domain.SafeSectionInfo;

import java.util.List;

public interface SafeLine {
    List<Long> getAllStationIds();
    List<SafeSectionInfo> getAllSafeSectionInfos();
}
