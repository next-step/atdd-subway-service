package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Sections {

  @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
  private List<Section> sections = new ArrayList<>();

  public Sections(List<Section> section) {
    this.sections = section;
  }

  public List<Station> getOrderedStations() {
    if (this.getSections().isEmpty()) {
      return Collections.emptyList();
    }

    List<Station> stations = new ArrayList<>();
    Station downStation = findUpStation();
    stations.add(downStation);

    while (downStation != null) {
      Station finalDownStation = downStation;
      Optional<Section> nextLineStation = this.getSections().stream()
          .filter(it -> it.getUpStation() == finalDownStation)
          .findFirst();
      if (!nextLineStation.isPresent()) {
        break;
      }
      downStation = nextLineStation.get().getDownStation();
      stations.add(downStation);
    }

    return stations;
  }

  public void add(Section section) {
    List<Station> stations = getOrderedStations();
    Station upStation = section.getUpStation();
    Station downStation = section.getDownStation();

    boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
    boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

    if (isUpStationExisted && isDownStationExisted) {
      throw new RuntimeException("이미 등록된 구간 입니다.");
    }

    if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
        stations.stream().noneMatch(it -> it == downStation)) {
      throw new RuntimeException("등록할 수 없는 구간 입니다.");
    }

    if (stations.isEmpty()) {
      sections.add(section);
      return;
    }

    if (isUpStationExisted) {
      sections.stream()
          .filter(it -> it.getUpStation() == upStation)
          .findFirst()
          .ifPresent(it -> it.updateUpStation(downStation, section.getDistance()));

      sections.add(section);
    } else if (isDownStationExisted) {
      sections.stream()
          .filter(it -> it.getDownStation() == downStation)
          .findFirst()
          .ifPresent(it -> it.updateDownStation(upStation, section.getDistance()));

      sections.add(section);
    } else {
      throw new RuntimeException();
    }

  }

  public void remove(Station station) {
    if (this.sections.size() <= 1) {
      throw new RuntimeException();
    }

    Optional<Section> upLineStation = this.sections.stream()
        .filter(it -> it.getUpStation() == station)
        .findFirst();
    Optional<Section> downLineStation = this.sections.stream()
        .filter(it -> it.getDownStation() == station)
        .findFirst();

    if (upLineStation.isPresent() && downLineStation.isPresent()) {
      Station newUpStation = downLineStation.get().getUpStation();
      Station newDownStation = upLineStation.get().getDownStation();
      int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
      this.sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
    }

    upLineStation.ifPresent(it -> this.sections.remove(it));
    downLineStation.ifPresent(it -> this.sections.remove(it));
  }

  private Station findUpStation() {
    Station downStation = this.getSections().get(0).getUpStation();
    while (downStation != null) {
      Station finalDownStation = downStation;
      Optional<Section> nextLineStation = this.getSections().stream()
          .filter(it -> it.getDownStation() == finalDownStation)
          .findFirst();
      if (!nextLineStation.isPresent()) {
        break;
      }
      downStation = nextLineStation.get().getUpStation();
    }

    return downStation;
  }

}
