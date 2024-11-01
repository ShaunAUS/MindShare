# MindShare

- 프로젝트 시작전 mysql과 redis를 실행 시켜 줍니다(저는 도커를 사용했습니다)
- 메일에 같이 보내드린 postman을 import하시고 사용하시면 됩니다.

# API

- 자세한건 같이 보내드린 postman을 참고해주세요

### MEMBER

- POST / : api/v1/member => 회원가입
- PATCH / : api/v1/member => 회원정보 수정

### BOARD

- GET / : api/v1/board/{board-uuid} => 게시글 조회
- GET / : api/v1/board/all => 게시글 전체 조회
- GET / : api/v1/board/search?searchTitle={title} => 게시글 검색
- PATCH / : api/v1/board => 게시글 수정
- PATCH / : api/v1/board/{board-uuid} => 게시글 삭제
- POST / : api/v1/board => 게시글 작성

### AUTH

- POST / : api/v1/auth/login => 로그인

### AUTH-TEST

- GET / : api/v1/member/test = > 권한 테스트

# 고민한 점이나 아쉬운 점

- GlobalException을 좀더 Readable하게 만들고 싶었지만, 깔끔한 방법이 생각이 안났습니다.
- 멀티모듈로 나눌때의 패키지 구조에 대한 고민이 있었음
- 조회나 수정요청시 pk값을 어떻게 보낼까 + pk값 대신에 다른걸 보낼까 고민
- 더티체킹을 믿어야하나 -> 명시성을 위해 저는 save를 사용했습니다
- 에러코드에 대한 고민 -> 저는 http status code를 일부로 200으로 내려 주고 있습니다. 해커가 봤을때 뭐가 오류가났는지 알지 못하도록 하기 위함입니다.

# ETC

- 환경변수는 따로 가리지 않았습니다
- 평소에는 이슈파고 그 이슈로 브랜치를 파고 거기서 작업을하고 pr올리고 머지하는 방식으로 코드를 작성 합니다. 이번에는 과제에 따로 언급이 없어서 그렇게 하지 않았습니다.
- 검색같은 경우는 별도의 조건이 표기되지않아 '제목' 을 검색하도록 구성했습니다.
