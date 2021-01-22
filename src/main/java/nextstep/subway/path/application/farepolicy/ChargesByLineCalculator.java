package nextstep.subway.path.application.farepolicy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class ChargesByLineCalculator {

    /**
     * 노선의 추가 요금을 구합니다.
     * 단, 여러 노선을 거치는 경우 가장 큰 추가요금을 반환합니다.
     * @param vertexList
     * @return
     */
    public static int getMaxLineFare(List<Station> vertexList, List<Line> persistLines) {
        Sections includedSections = getIncludedSections(vertexList);
        int max = 0;
        for (Line line : persistLines) {
            max = Math.max(max, line.getSections().stream()
                    .filter(includedSections::existSection)
                    .map(Section::getSurcharge).distinct()
                    .max(Integer::compareTo).orElse(0));
        }

        return max;
    }

    /**
     * 최단경로를 찾는 구간의 상행역, 하행역 정보로 바꿉니다.
     * @param vertexList
     * @return
     */
    private static Sections getIncludedSections(List<Station> vertexList) {
        List<Section> includedSections = new ArrayList<>();

        vertexList.stream().reduce((upStation, downStation)
                -> { includedSections.add(new Section(new Line(), upStation, downStation, 0));
            return downStation;
        });

        return new Sections(includedSections);
    }
}
