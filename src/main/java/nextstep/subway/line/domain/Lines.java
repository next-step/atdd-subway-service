package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.domain.Station;

public final class Lines {

    private final List<Line> list;

    private Lines(List<Line> list) {
        Assert.notNull(list, "지하철 노선 목록이 null 일 수 없습니다.");
        this.list = list;
    }

    public static Lines from(List<Line> list) {
        return new Lines(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<Station> stationList() {
        List<Station> sectionList = new ArrayList<>();
        for (Line line : list) {
            sectionList.addAll(line.stationList());
        }
        return sectionList;
    }

    public List<Section> sectionList() {
        List<Section> sectionList = new ArrayList<>();
        for (Line line : list) {
            sectionList.addAll(line.sectionList());
        }
        return sectionList;
    }
}
