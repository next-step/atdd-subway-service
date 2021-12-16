package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
  private static final int DELETABLE_SIZE = 2;
  private static final int SECTION_FIRST_INDEX = 0;

  @OneToMany(mappedBy = "line", cascade = {CascadeType.ALL}, orphanRemoval = true)
  private List<Section> sections = new ArrayList<>();

  protected Sections() {
  }

  public Sections(List<Section> sections) {
    sections.addAll(sections);
  }

  public void add(Section section) {
    if (isUpdateSection(section)) {
      checkAddSectionValidation(section);
      updateSection(section);
    }
    sections.add(section);
  }

  public void remove(Station station) {
    checkRemovableSections(station);
    if (isEndPointStation(station)) {
      removeEndPointSectionByStation(station);
      return;
    }

    removeAndUpdateSectionByStation(station);
  }

  public List<Station> getOrderedStations() {
    List<Section> orderedSections = getOrderedSections();
    if (orderedSections.isEmpty()) {
      return Collections.emptyList();
    }

    List<Station> stations = new ArrayList<>();
    stations.add(orderedSections.get(SECTION_FIRST_INDEX).getUpStation());
    stations.addAll(orderedSections.stream()
            .map(Section::getDownStation)
            .collect(Collectors.toList()));

    return stations;
  }

  public List<Section> getOrderedSections() {
    if (sections.isEmpty()) {
      return Collections.emptyList();
    }

    List<Section> sections = new ArrayList<>();
    sections.add(upEndPoint());
    while (!hasDownEndPointSection(sections)) {
      sections.add(findNextLinkedSection(sections));
    }

    return sections;
  }

  public boolean containsAll(List<Station> stations) {
    return getOrderedStations().containsAll(stations);
  }

  private void checkRemovableSections(Station station) {
    if (sections.size() < DELETABLE_SIZE) {
      throw new RuntimeException("구간이 하나일 경우, 지하철 역을 제거할 수 없습니다.");
    }
    if (!getOrderedStations().contains(station)) {
      throw new RuntimeException("노선에 등록되지 않은 역은 제거할 수 없습니다.");
    }
  }

  private boolean isEndPointStation(Station station) {
    return isUpEndPointStation(station)
            || isDownEndPointStation(station);
  }

  private void removeEndPointSectionByStation(Station station) {
    if (isUpEndPointStation(station)) {
      sections.removeIf(section -> section.equals(upEndPoint()));
      return;
    }

    sections.removeIf(section -> section.equals(downEndPoint()));
  }

  private void removeAndUpdateSectionByStation(Station station) {
    Section removeTargetSection = sections.stream()
            .filter(section -> section.getUpStation().equals(station))
            .findFirst()
            .orElseThrow(IllegalStateException::new);
    sections.remove(removeTargetSection);
    updateByRemoveTargetSection(station, removeTargetSection);
  }

  private void updateSection(Section newSection) {
    if (hasMatchUpStation(newSection)) {
      updateMatchedUpSideSection(newSection);
      return;
    }

    updateMatchedDownSideSection(newSection);
  }

  private boolean hasMatchUpStation(Section section) {
    return sections.stream()
            .map(Section::getUpStation)
            .anyMatch(station -> station.equals(section.getUpStation()));
  }

  private void updateByRemoveTargetSection(Station station, Section removeTargetSection) {
    sections.stream()
            .filter(section -> section.getDownStation().equals(station))
            .findFirst()
            .ifPresent(section -> section.updateDownStation(removeTargetSection.getDownStation(), removeTargetSection.getDistance()));
  }

  private void checkAddSectionValidation(Section section) {
    if (!containsStation(section)) {
      throw new RuntimeException("등록할 수 없는 구간 입니다.");
    }

    if (containSection(section)) {
      throw new RuntimeException("이미 등록된 구간 입니다.");
    }
  }

  private void updateMatchedUpSideSection(Section newSection) {
    sections.stream()
            .filter(section -> section.getUpStation().equals(newSection.getUpStation()))
            .findFirst()
            .ifPresent(section -> section.updateUpSideSection(newSection));
  }

  private void updateMatchedDownSideSection(Section newSection) {
    sections.stream()
            .filter(section -> section.getDownStation().equals(newSection.getDownStation()))
            .findFirst()
            .ifPresent(section -> section.updateDownSideSection(newSection));
  }

  private boolean containSection(Section section) {
    return sections.stream()
            .anyMatch(existSection -> existSection.isMatch(section));
  }

  private boolean containsStation(Section section) {
    List<Station> orderedStations = getOrderedStations();
    if (orderedStations.contains(section.getUpStation())) {
      return true;
    }

    return orderedStations.contains(section.getDownStation());
  }

  private boolean isUpdateSection(Section section) {
    return !sections.isEmpty()
            && !isUpEndPointStation(section.getDownStation())
            && !isDownEndPointStation(section.getUpStation());
  }

  private boolean isDownEndPointStation(Station station) {
    return downEndPoint().getDownStation().equals(station);
  }

  private boolean isUpEndPointStation(Station station) {
    return upEndPoint().getUpStation().equals(station);
  }

  private Section upEndPoint() {
    return sections.stream()
            .filter(this::isUpEndPoint)
            .findAny()
            .orElseThrow(IllegalStateException::new);
  }

  private boolean isUpEndPoint(Section parentSection) {
    return sections.stream()
            .map(Section::getDownStation)
            .noneMatch(station -> station.equals(parentSection.getUpStation()));
  }

  private Section downEndPoint() {
    return sections.stream()
            .filter(this::isDownEndPoint)
            .findAny()
            .orElseThrow(IllegalStateException::new);
  }

  private boolean isDownEndPoint(Section parentSection) {
    return sections.stream()
            .map(Section::getUpStation)
            .noneMatch(station -> station.equals(parentSection.getDownStation()));
  }

  private boolean hasDownEndPointSection(List<Section> sections) {
    return sectionsLastElement(sections).equals(downEndPoint());
  }

  private Section sectionsLastElement(List<Section> sections) {
    return sections.get(sections.size() - 1);
  }

  private Section findNextLinkedSection(List<Section> sections) {
    return this.sections.stream()
            .filter(section -> section.getUpStation().equals(sectionsLastElement(sections).getDownStation()))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
  }
}
