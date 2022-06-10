package nextstep.subway.line.utils;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.util.Comparator;

public class SectionsComparator implements Comparator<Section> {
    @Override
    public int compare(Section o1, Section o2) {
        Station downStation = o1.getDownStation();
        Station upStation = o2.getUpStation();

        if (!downStation.getName().equals(upStation.getName())) {
            return 1;
        } else {
            return -1;
        }
    }

}
