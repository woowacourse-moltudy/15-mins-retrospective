# 15-mins-retrospective season1

🚀 [레포지토리 링크](https://github.com/woowacourse-moltudy/15-mins-retrospective)

## 구현해야 할 기능
1. 시간을 선택하는 기능
2. 페어매칭 기능
3. 이름을 입력하여 로그인하는 기능

## 데일리 회고

- 매일 10시 슬랙 채널과 스레드를 이용해 앞으로 할 내용, 한 내용, 논의해볼 내용 등을 공유한다.

## 프로젝트 진행

- 깃허브의 [이슈](https://github.com/woowacourse-moltudy/15-mins-retrospective/issues), [프로젝트](https://github.com/woowacourse-moltudy/15-mins-retrospective/projects), [위키](https://github.com/woowacourse-moltudy/15-mins-retrospective/wiki) 를 적극적으로 활용한다.
- 논의할 일이 있으면 주제를 정해 zoom으로 회의한다. 회의는 짧게!

## 커밋 컨벤션

- 기능 단위 별로 브랜치를 나누어 진행한다.
> feature/member-login, feature/fare-matching

##  코드 컨벤션

- 클래스 내부 첫 줄과 마지막 줄을 개행
- 주 생성자는 부 생성자 보다 밑에 위치
- 패키지 구조는 레이어 별로 구성
- 문서화는 controller 레이어를 기준으로  
- 예외는 공통 예외를 활용하도록 노력  
- 세로 정렬시 첫 줄 같은 요소 왼쪽 끝에 맞출 것
    ```java
    private static String test(Map<String, Object> model, 
                               String templatePath, 
                               String test1, 
                               String test2, 
                               String test3) {
           return "test";
    }
    ```

- 의미있는 공백라인 활용하기
    ```java
    static String test() {
           //바인딩
           String a = "test";
           String b = "test2";
                
           //기능
           String c = a + b;
                
           //반환
           return c;
    }
    ```
    위와 같이  바인딩, 기능, 반환을 기준으로 나눈다.
    inline이 가능하다면 inline으로 작성한다.
