package nextstep.subway.path.domain.adapters;

import nextstep.subway.path.domain.SafeStationInfo;

import java.util.List;

public interface SafeStation {
    List<SafeStationInfo> findStationsById(List<Long> stationIds);
}
