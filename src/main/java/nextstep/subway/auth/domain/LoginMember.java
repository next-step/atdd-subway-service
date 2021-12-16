package nextstep.subway.auth.domain;

public class LoginMember {
  private Long id;
  private String email;
  private Integer age;
  private Role role;

  public LoginMember() {
  }

  public LoginMember(Long id, String email, Integer age, Role role) {
    this.id = id;
    this.email = email;
    this.age = age;
    this.role = role;
  }

  public static LoginMember ofGuest() {
    return new LoginMember(null, null, null, Role.GUEST);

  }

  public boolean isUser() {
    return role.equals(Role.USER);
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public Integer getAge() {
    return age;
  }
}
