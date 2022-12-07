package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AdditionalFareByLinePolicy implements AdditionalFarePolicy {

    private static final int SLIDING_WINDOW_SIZE = 2;
    private static final int UP_STATION_INDEX = 0;
    private static final int DOWN_STATION_INDEX = 1;
    private static final int INDEX_START_INCLUSIVE = 0;
    private static final int INDEX_CORRECTION_VALUE = 1;

    @Override
    public int addFare(Sections sections, List<Station> paths) {
        return sliding(paths)
                .map(subList -> sections.getLineByStations(subList.get(UP_STATION_INDEX), subList.get(DOWN_STATION_INDEX)))
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElseThrow(IllegalStateException::new);
    }

    private Stream<List<Station>> sliding(List<Station> paths) {
        return IntStream.range(INDEX_START_INCLUSIVE, paths.size() - SLIDING_WINDOW_SIZE + INDEX_CORRECTION_VALUE)
                .mapToObj(start -> paths.subList(start, start + SLIDING_WINDOW_SIZE));
    }
}
