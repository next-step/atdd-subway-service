package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class SectionConnectManager {
    public static void connectAll(Line line, Section request, List<Section> matchedSections) {
        List<Section> requests = matchedSections.stream()
                .map(section -> SectionConnectManager.connect(section, request))
                .filter(Objects::nonNull)
                .collect(toList());

        addNewSections(line, requests);
    }

    private static Section connect(Section current, Section request) {
        ConnectionType connectionType = ConnectionType.match(current, request);
        connectionType.connect(current, request);
        return request;
    }

    private static void addNewSections(Line line, List<Section> requests) {
        requests.forEach(request -> line.addSection(request));
    }
}
