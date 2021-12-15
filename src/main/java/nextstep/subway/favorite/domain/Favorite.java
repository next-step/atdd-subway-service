package nextstep.subway.favorite.domain;

import nextstep.subway.ServiceException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Entity
public class Favorite {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  protected Favorite() {
  }

  private Favorite(Long id, Member member, Station sourceStation, Station targetStation) {
    this.id = id;
    this.member = member;
    this.sourceStation = sourceStation;
    this.targetStation = targetStation;
  }

  private Favorite(Member member, Station sourceStation, Station targetStation) {
    this(null, member, sourceStation, targetStation);
  }

  public static Favorite of(Member member, Station sourceStation, Station targetStation) {
    return new Favorite(member, sourceStation, targetStation);
  }

  public Long getId() {
    return id;
  }

  public Station getTargetStation() {
    return targetStation;
  }

  public Station getSourceStation() {
    return sourceStation;
  }

  public Member getMember() {
    return member;
  }

  public void checkDuplicate(Station sourceStation, Station targetStation) {
    if (this.sourceStation.equals(sourceStation) && this.targetStation.equals(targetStation)) {
      throw new ServiceException(HttpStatus.BAD_REQUEST, "동일한 출발역과 목적역이 설정된 즐겨찾기 항목이 있습니다.");
    }
  }
}
