# Stock-batch DashBoard (최신화 2024/05/24)

### 서비스 내용
- 앞서 만들었던 [Notity](https://github.com/yongjung95/notify)의 주식 종목 정보들을 하루에 한 번 (자정)에 업데이트.
- 신규 상장 종목들이 자동으로 업데이트 됨.


### 기술 스택
- Java 17, 전자공시 다트 API, Spring Boot 3.2.5
- Mysql, Github Actions

### 아쉬운 점
- Spring Batch를 사용해서 작업을 하고 싶었으나, 일단은 스케쥴링을 통해 작업 진행
  - 추후 Spring Batch로 변경 예정
