package nextstep.subway.station.dto;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponses {

    private List<StationResponse> stationResponses;

    public StationResponses() {
    }

    public StationResponses(final List<Station> list) {
        this.stationResponses = list.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponses(final Sections sections) {
        stationResponses = new ArrayList<>();
        sections.getSections()
                .stream()
                .sorted(Comparator.comparing(Section::getId).reversed())
                .forEach(section -> {
                    addIfAbsent(StationResponse.of(section.getUpStation()));
                    addIfAbsent(StationResponse.of(section.getDownStation()));
                });
    }

    private void addIfAbsent(final StationResponse response) {
        boolean noneMatch = stationResponses
                .stream()
                .noneMatch(stationResponse -> stationResponse.getId().equals(response.getId()));

        if (noneMatch) {
            stationResponses.add(response);
        }
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int size() {
        return stationResponses.size();
    }

}
