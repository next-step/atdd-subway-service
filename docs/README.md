
## FeatureList

- 즐겨찾기 관련 기능 (/favorites)
    * [X] 사용자의 즐겨찾기 목록조회
        
    * [X] 사용자의 즐겨찾기 등록한다.
        * [X] 예외 상황
    
          |내용   |Exception|
          |---|---|
          | 출발&도착을 동일한 역으로 즐겨찾기를 등록할 경우 |IllegalArgumentException|
      
    * [X] 사용자의 즐겨찾기 삭제 한다.
        * [X] 예외 상황
    
          |내용   |Exception|
          |---|---|
          | 등록 되어있지 않은 즐겨찾기를 삭제할 경우 |IllegalArgumentException|
            
- 나의정보 관련 기능 (/members/me)
    * [X] 나의정보 조회,수정,삭제 는 인증 토큰을 기반으로 한다.
        * [X] 예외 상황
        
          |내용   |Exception|
          |---|---|
          |유효하지 않은 인증 토큰인 경우(+만료된 토큰) |AuthorizationException|
