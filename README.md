# 🚀 Dopamine

**Dopamine**은 반복적인 설정과 구조 작업을 줄이고,  
**개발자가 더 중요한 문제 해결에 집중할 수 있도록 설계된 공통 인프라 프레임워크**입니다.

번아웃을 겪던 시기에 다시 개발에 집중하고 싶은 마음으로 시작했고,  
같은 고민을 겪는 개발자들에게도 도움이 되기를 기대합니다.

---

## 🎯 목표

* 실무에서 자주 사용되는 기능들을 공통화
* 개발자의 생산성을 높이는 자동화된 구조 제공
* `spring-boot-starter-*` 방식의 모듈화
* 설정만으로 기능 활성/비활성 가능

---

## 🎯 Features

✔️ 공통 응답 포맷 자동 래핑 (`DopamineResponse<T>`)  
✔️ traceId 자동 생성 및 응답/로그 포함  
✔️ 예외 자동 포맷 처리 (비즈니스 & 시스템 오류 구분)  
✔️ 국제화(i18n) 메시지 바인딩 지원  
✔️ Kotlin + Gradle (KTS) + Kotest 기반 테스트 환경  
✔️ 설정 기반 조건부 기능 비활성화 가능

---

## 🧩 모듈 구성

| 모듈                   | 역할 설명 |
|------------------------|-----------|
| `trace-common`         | traceId 생성 및 추상화 (UUID, 커스텀 등) |
| `trace-mvc`            | Servlet(MDC) 기반 traceId 추출 및 필터 구성 |
| `i18n`                 | 메시지 국제화 설정 및 다국어 바인딩 지원 |
| `response-common`      | 공통 응답 포맷(`DopamineResponse`) 및 meta 구조 정의 |
| `response-mvc`         | 응답/예외 자동 래핑 `@RestControllerAdvice` 구성 |
| `docs`                 | Swagger 자동 구성 및 기본 그룹 설정 |
| `starter-common`       | 공통 설정 등 부가 기능 통합 구성 |
| `starter-mvc`          | `trace-mvc`, `response-mvc`, `starter-common` 자동 설정 통합 |
| `sample`               | starter 의존성 기반 통합 동작 검증 샘플 앱 |

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

## 🛠️ 향후 개발 예정
- dopamine-auth: JWT / 세션 기반 인증
- dopamine-docs: Swagger 문서 + Markdown 가이드 구성
- dopamine-file: 파일 업로드 및 응답 자동 포맷
- dopamine-admin: 설정/헬스 정보 확인 인터페이스
- 알림, 캐시, 배치 등 실무 필수 기능 모듈 예정

---

## ⚙️ 기술 스택

* **Spring Boot 3.4.5**
* **Kotlin: 2.1.0**
* **Gradle (Kotlin DSL)**
* **Kotest**

---

## 🙋‍♂️ Maintainer

* DY (a.k.a dy.log)
* 기술 블로그: [https://velog.io/@dylog/posts](https://velog.io/@dylog/posts)

---

## 📜 라이선스

MIT License
