package nextstep.subway.line.step;

import static nextstep.subway.station.step.StationStep.station;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;

public final class SectionStep {

    public static Section section(String upStation, String downStation, Integer distance) {
        return Section.of(station(upStation), station(downStation), Distance.from(distance));
    }
}
