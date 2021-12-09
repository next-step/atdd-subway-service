package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionResponse(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse of(Section section) {
        return new SectionResponse(section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance().getDistance());
    }

    public static List<SectionResponse> listOf(List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
