# Dopamine

> 🚧 **This project is a work in progress.**
> 기능은 순차적으로 개발 중이며, 구조와 내용은 변경될 수 있습니다.

**Dopamine**은 반복적인 개발 업무를 줄이고, 개발자에게 다시 동기부여와 의욕을 불어넣을 수 있는 **Spring Boot 스타터킷 기반 프레임워크**입니다.

이 프로젝트는 번아웃이 찾아오던 시기에, 스스로에게 다시 에너지를 줄 수 있는 무언가를 만들고 싶다는 마음에서 출발했습니다. 그리고 이 프레임워크를 사용하는 개발자들도 **반복적인 작업에서 벗어나 더 중요한 일에 집중하며 동기를 되찾을 수 있기를** 바라는 마음이 담겨 있습니다.

현재는 핵심 기능 개발 단계이며, 향후에는 상용 서비스에서도 적용 가능한 품질과 유연성을 갖춘 구조로 발전시키는 것을 지향합니다.

---

## 🎯 목표

* 실무에서 자주 사용되는 기능들을 공통화
* 개발자의 생산성을 높이는 자동화된 구조 제공
* `spring-boot-starter-*` 방식의 모듈화
* 설정만으로 기능 활성/비활성 가능

---

## ⚙️ 기술 스택

* **Spring Boot 3.x**
* **Kotlin** + **Gradle (Kotlin DSL)**
* **Kotest** 기반 테스트 통일

---

## 🧩 주요 모듈 구성

현재 개발 중인 주요 모듈과 그 역할은 다음과 같습니다:

| 모듈              | 설명                                       |
| --------------- | ---------------------------------------- |
| `trace-core`    | traceId 생성 및 저장에 대한 추상화 구성               |
| `trace-mvc`     | MDC 기반 traceId 저장 및 추출 필터 구성             |
| `response-core` | `DopamineResponse<T>` 포맷 정의 및 meta 구조 제공 |
| `response-mvc`  | 컨트롤러 응답 자동 래핑 및 예외 처리 Advice 구성          |
| `starter-mvc`   | response + trace 조합 자동 설정 구성             |
| `sample`        | 실제 연동 흐름을 검증하는 샘플 애플리케이션                 |

---

## 🧪 테스트 전략

Dopamine은 설정 바인딩, 자동 응답 래핑, traceId 포함 여부 등 다양한 항목에 대해 유닛/통합 테스트를 제공합니다.

| 항목             | 방식     | 설명                                             |
| -------------- | ------ | ---------------------------------------------- |
| 응답 구조 유닛 테스트   | ✅      | `DopamineResponse` 직렬화가 정상적으로 동작하는지 확인         |
| 자동 응답 래핑 적용 여부 | ✅ (통합) | 컨트롤러 응답이 자동으로 래핑되는지 통합 테스트로 검증                 |
| traceId 포함 여부  | ✅ (통합) | meta.traceId가 응답에 포함되는지 확인                     |
| 설정 바인딩 테스트     | ✅      | `ResponseProperties`가 Spring 설정으로 정상 바인딩되는지 검증 |
| 조건부 빈 등록 테스트   | ✅      | `enabled=false` 설정 시 관련 Bean이 등록되지 않는지 검증      |

---

## 🏗️ 향후 개발 계획

* `dopamine-auth`: JWT / 세션 기반 인증 및 인가 기능
* `dopamine-admin`: 설정 정보 및 관리용 인터페이스 제공
* 파일 처리, 알림, 캐시, 배치 등 실무에서 자주 쓰이는 기능 모듈 순차 개발 예정

---

## 📂 샘플 실행 방법

```bash
./gradlew :dopamine-starter-mvc-sample:bootRun
```

다음 엔드포인트로 샘플 응답 포맷을 확인할 수 있습니다:

```
GET http://localhost:8080/sample/dto
```

예상 응답 예시 (일부 축약):

```json
{
  "code": "SUCCESS_200",
  "message": "Request was successful.",
  "data": {
    "id": 42,
    "name": "Dopamine",
    "status": "ACTIVE",
    "createdAt": "2025-06-01T12:13:53.9964288"
  },
  "timestamp": "2025-06-01T12:13:54",
  "meta": {
    "traceId": "b99b6bc6-c05d-4c9d-ae20-e5d27f58a49a"
  }
}
```

---

## 📜 라이선스

MIT License

---

## 🙋‍♂️ Maintainer

* DY (a.k.a 디와이.log)
* 기술 블로그: [https://dylog.dev](https://dylog.dev) *(예정)*
