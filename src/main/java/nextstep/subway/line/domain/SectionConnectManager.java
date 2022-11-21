package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SectionConnectManager {
    public static void connectAll(Line line, Section request, List<Section> matchedSections) {
        Set<Section> requests = matchedSections.stream()
                .map(section -> SectionConnectManager.connect(section, request))
                .filter(Objects::nonNull)
                .collect(toSet());

        addNewSections(line, requests);
    }

    private static Section connect(Section current, Section request) {
        ConnectionType connectionType = ConnectionType.match(current, request);
        connectionType.connect(current, request);
        return request;
    }

    private static void addNewSections(Line line, Set<Section> requests) {
        requests.forEach(request -> line.addSection(request));
    }
}
