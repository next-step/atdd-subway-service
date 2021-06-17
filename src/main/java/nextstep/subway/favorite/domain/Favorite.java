package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "source_station_id")
  private Station sourceStation;

  @ManyToOne
  @JoinColumn(name = "target_station_id")
  private Station targetStation;

  protected Favorite() {}

  public Favorite(Member member, Station sourceStation, Station targetStation) {
    this.member = member;
    this.sourceStation = sourceStation;
    this.targetStation = targetStation;
  }

  public Favorite(Long id, Member member, Station sourceStation, Station targetStation) {
    this.id = id;
    this.member = member;
    this.sourceStation = sourceStation;
    this.targetStation = targetStation;
  }

  public Long getId() {
    return id;
  }

  public Station getSourceStation() {
    return sourceStation;
  }

  public Station getTargetStation() {
    return targetStation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Favorite favorite = (Favorite) o;
    return Objects.equals(id, favorite.id) && Objects.equals(member, favorite.member) && Objects.equals(sourceStation, favorite.sourceStation) && Objects.equals(targetStation, favorite.targetStation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, member, sourceStation, targetStation);
  }
}
