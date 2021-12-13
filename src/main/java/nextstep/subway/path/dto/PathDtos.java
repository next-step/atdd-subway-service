package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Section;

public class PathDtos {
    private List<PathDto> pathDtos;

    private PathDtos(List<PathDto> pathDtos) {
        this.pathDtos = pathDtos;
    }

    public static PathDtos from(List<Section> sections) {
        List<PathDto> pathDtos = sections.stream()
            .map(PathDto::from)
            .collect(Collectors.toList());

        return new PathDtos(pathDtos);
    }

    public List<PathDto> getPathDtos() {
        return pathDtos;
    }
}
