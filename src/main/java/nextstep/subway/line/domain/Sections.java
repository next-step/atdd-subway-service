package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.hibernate.annotations.SortNatural;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

  private static final int SINGLE_ELEMENT_SIZE = 1;
  private static final String DUPLICATED_STATIONS = "이미 등록된 역 구간을 다시 등록 할 수 없습니다.";
  private static final String NOT_CONTAINS_NEITHER_STATIONS = "상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없습니다.";
  private static final String CAN_NOT_REMOVE_NON_REGISTERED_STATION = "등록되어 있지 않은 역은 제거할 수 없습니다.";
  private static final String CAN_NOT_REMOVE_STATION_FROM_SINGLE_SECTION = "상행 종점 - 하행 종점으로 이루어진 하나의 구간만 있을 때는 역을 제거할 수 없습니다.";

  @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
  @SortNatural
  private SortedSet<Section> lineSections = new TreeSet<>();

  public Sections() {
  }


  public void registerNewSection(Section newSection) {
    if (lineSections.isEmpty()) {
      this.lineSections.add(newSection);
      return;
    }
    registerNewSectionToNotEmptySections(newSection);
  }

  public Set<Station> getDistinctStations() {
    return lineSections.stream()
        .flatMap(Section::getUpAndDownStationStream)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void removeStation(Station stationForRemove) {
    validateBeforeRemoveStation(stationForRemove);
    if (isUpStationEdge(stationForRemove)) {
      lineSections.remove(lineSections.first());
      return;
    }
    if (isDownStationEdge(stationForRemove)) {
      lineSections.remove(lineSections.last());
      return;
    }
    removeStationNotEachEdge(stationForRemove);
  }

  private void registerNewSectionToNotEmptySections(Section newSection) {
    validateNewSection(newSection);
    this.lineSections
        .forEach(lineSection -> lineSection.insertNewSection(newSection));
    this.lineSections.add(newSection);
  }

  private void validateNewSection(Section newSection) {
    if (hasBothStations(newSection)) {
      throw new IllegalArgumentException(DUPLICATED_STATIONS);
    }
    if (hasNotBothStations(newSection)) {
      throw new IllegalArgumentException(NOT_CONTAINS_NEITHER_STATIONS);
    }
  }

  private boolean hasBothStations(Section section) {
    return !notContainsStation(section.getUpStation()) && !notContainsStation(section.getDownStation());
  }

  private boolean hasNotBothStations(Section section) {
    return notContainsStation(section.getUpStation()) && notContainsStation(section.getDownStation());
  }

  private boolean notContainsStation(Station station) {
    return lineSections.stream()
        .noneMatch(lineSection -> lineSection.containsStation(station));
  }

  private void validateBeforeRemoveStation(Station stationForRemove) {
    validateSingleSection();
    validateNonRegisteredStation(stationForRemove);
  }

  private void validateSingleSection() {
    if (lineSections.size() <= SINGLE_ELEMENT_SIZE) {
      throw new IllegalArgumentException(CAN_NOT_REMOVE_STATION_FROM_SINGLE_SECTION);
    }
  }

  private void validateNonRegisteredStation(Station stationForRemove) {
    if (lineSections.stream().noneMatch(section -> section.containsStation(stationForRemove))) {
      throw new IllegalArgumentException(CAN_NOT_REMOVE_NON_REGISTERED_STATION);
    }
  }

  private boolean isUpStationEdge(Station stationForRemove) {
    return lineSections.first()
        .containsAsUpStation(stationForRemove);
  }

  private boolean isDownStationEdge(Station stationForRemove) {
    return lineSections.last()
        .containsAsDownStation(stationForRemove);
  }

  private void removeStationNotEachEdge(Station stationForRemove) {
    List<Section> stationContainingSortedSections = getStationContainingSortedSections(stationForRemove);
    Section toUpdateSection = stationContainingSortedSections.get(0);
    Section toRemoveSection = stationContainingSortedSections.get(1);
    toUpdateSection.removeStationBetweenSections(toRemoveSection);
    lineSections.remove(toRemoveSection);
  }

  private List<Section> getStationContainingSortedSections(Station targetStation) {
    return lineSections.stream()
        .filter(section -> section.containsStation(targetStation))
        .collect(Collectors.toList());
  }
}
