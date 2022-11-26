package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SectionConnectManager {
    public static void connectAll(Line line, Section request, List<Section> matchedSections) {
        matchedSections.forEach(section -> SectionConnectManager.connect(section, request));
        line.addSection(request);
    }

    private static void connect(Section current, Section request) {
        ConnectionType connectionType = ConnectionType.match(current, request);
        connectionType.connect(current, request);
    }
}
