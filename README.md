# 🌍 Holiday Management Service

전 세계 공휴일 정보를 외부 API로부터 수집하고 저장/조회할 수 있는 RESTful 백엔드 서비스입니다.  
Java 21 & Spring Boot 기반으로 구현되었으며, JPA, H2, Swagger(OpenAPI)를 활용합니다.

---

## 🚀 Build & Run

```bash
# 프로젝트 클론 및 실행
$ ./gradlew clean build
$ ./gradlew bootRun
```

- Java: 21
- Spring Boot: 3.4.x
- 실행 포트: `8080`

---

## 🛢️ H2 In-Memory DB 정보

- 콘솔: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`
- 사용자명: `sa`
- 비밀번호: *(비워둠)*

---

## 🧪 테스트 성공 스크린샷

`./gradlew clean test` 실행 결과:

> ✅ *이곳에 테스트 성공 스크린샷 첨부 예정*

---

## 🔍 Swagger & OpenAPI 확인

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🛠️ 기술 스택

| 항목        | 내용                    |
|-------------|-------------------------|
| 언어        | Java 21                 |
| 프레임워크  | Spring Boot 3.4.x       |
| 데이터베이스| H2 In-Memory DB         |
| ORM         | Spring Data JPA (Hibernate) |
| 문서화      | SpringDoc OpenAPI 2.3   |
| 기타        | Lombok, RestControllerAdvice |

---

## ✨ 주요 기능 요약

- 외부 공공 API로부터 공휴일 동기화
- 국가/연도 기반 검색 및 필터링
- 대량 비동기 동기화 지원 (`@Async`)
- 공통 예외 처리 및 응답 포맷 통일

---

## 📡 Holiday 동기화 API 명세

공휴일 정보를 외부 API로부터 가져와 데이터베이스에 저장합니다.  
사용 목적에 따라 다양한 동기화 옵션을 제공합니다.

---

### 🔄 1. 특정 연도+국가 공휴일 동기화  
**[POST] /api/holidays/sync**

- **설명**: 특정 연도와 국가의 공휴일을 외부 API에서 조회 후 저장  
- **요청 파라미터**
  - `year` (int, 필수) – 동기화할 연도 (예: `2025`)
  - `countryCode` (string, 필수) – 국가 코드 (ISO-2, 예: `KR`)
- **응답 예시**
```json
{
  "status": 200,
  "success": true,
  "message": null,
  "data": null,
  "error": null,
  "timestamp": "2025-07-06T22:40:20.532808"
}
```

---

### 🌐 2. 특정 연도 전체 국가 공휴일 동기화  
**[POST] /api/holidays/sync/year**

- **설명**: 주어진 연도에 대해 사용 가능한 모든 국가의 공휴일을 동기화  
- **요청 파라미터**
  - `year` (int, 필수) – 동기화할 연도
- **응답 예시**: 위와 동일

---

### 🗺️ 3. 특정 국가 최근 5개년 공휴일 동기화  
**[POST] /api/holidays/sync/country**

- **설명**: 특정 국가의 최근 5개년 공휴일을 동기화  
- **요청 파라미터**
  - `countryCode` (string, 필수) – 국가 코드 (ISO-2)
- **응답 예시**: 위와 동일

---

### 🚀 4. 전체 국가의 최근 5개년 대량 비동기 동기화  
**[POST] /api/holidays/sync/bulk**

- **설명**: 모든 국가의 최근 5개년 공휴일을 비동기로 병렬 동기화  
- **요청 파라미터**: 없음  
- **응답 예시**: 위와 동일

---


## 📦 Holiday 관리 API 명세

공휴일 데이터를 삭제하거나 외부 API를 통해 최신 정보로 갱신합니다.  
스케줄링이나 수동 동기화 시 활용됩니다.

---

### 🗑️ 1. 특정 연도+국가 공휴일 삭제  
**[DELETE] /api/holidays**

- **설명**: 특정 연도와 국가 코드에 해당하는 모든 공휴일 데이터를 삭제합니다.
- **요청 파라미터**
  - `year` (int, 필수) – 삭제 대상 연도 (예: `2023`)
  - `countryCode` (string, 필수) – 국가 코드 (ISO-2, 예: `KR`)
- **주의사항**  
  - API 호출 전 삭제 대상 데이터가 존재하는지 확인 필요
- **응답 예시**
```json
{
  "status": 200,
  "success": true,
  "message": null,
  "data": null,
  "error": null,
  "timestamp": "2025-07-06T23:00:00.000000"
}
```

---

### 🔄 2. 특정 연도+국가 공휴일 갱신  
**[PUT] /api/holidays/refresh**

- **설명**: 해당 연도와 국가의 기존 데이터를 삭제한 뒤 외부 API를 호출하여 재적재합니다.  
- **요청 파라미터**
  - `year` (int, 필수) – 갱신 대상 연도 (예: `2024`)
  - `countryCode` (string, 필수) – 국가 코드 (ISO-2, 예: `US`)
- **특징**
  - 내부 재처리 로직으로, 자동화 스케줄러에서도 활용 가능합니다.
- **응답 예시**
```json
{
  "status": 200,
  "success": true,
  "message": null,
  "data": null,
  "error": null,
  "timestamp": "2025-07-06T23:00:00.000000"
}
```

---

## 🔍 공휴일 검색 API 명세

연도와 국가 코드 기준으로 공휴일 목록을 조회할 수 있는 검색 API입니다.  
필터링 및 페이징 기능을 제공합니다.

---

### 🔎 1. 공휴일 검색  
**[GET] /api/holidays**

- **설명**: 특정 연도 및 국가 코드 기반으로 공휴일 목록을 검색합니다.
- **추가 필터링 옵션**:
  - 날짜 범위 (`from`, `to`)
  - 공휴일 유형 (`types`) — 예: `Public`, `Bank`, `School`, ...
- **페이징 옵션**:
  - `page` (0부터 시작)
  - `size` (페이지 당 항목 수)

---

#### ✅ 요청 파라미터

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|----------|------|------|------|------|
| `year` | int | ✅ | 조회할 연도 | `2025` |
| `countryCode` | string | ✅ | ISO 2자리 국가코드 | `KR` |
| `from` | string (yyyy-MM-dd) | ❌ | 검색 시작일 | `2025-01-01` |
| `to` | string (yyyy-MM-dd) | ❌ | 검색 종료일 | `2025-12-31` |
| `types` | string[] | ❌ | 공휴일 유형 필터 | `Public` |
| `page` | int | ❌ | 페이지 번호 (0부터 시작) | `1` |
| `size` | int | ❌ | 페이지당 데이터 수 | `10` |

---

#### 📥 요청 예시 (curl)
```bash
curl -X 'GET' \
  'http://localhost:8080/api/holidays?year=2025&countryCode=KR&from=2025-01-01&to=2025-12-31&types=Public&page=1&size=10' \
  -H 'accept: application/json'
```

---

#### 📤 응답 예시
```json
{
  "status": 200,
  "success": true,
  "message": null,
  "data": {
    "content": [
      {
        "id": 5443,
        "date": "2025-10-06",
        "localName": "추석",
        "name": "Chuseok",
        "countryCode": "KR",
        "fixed": false,
        "global": true,
        "counties": [],
        "launchYear": null,
        "types": ["Public"]
      },
      ...
    ],
    "pageable": {
      "pageNumber": 1,
      "pageSize": 10,
      "offset": 10,
      ...
    },
    "totalElements": 45,
    "totalPages": 5,
    "size": 10,
    "number": 1,
    "numberOfElements": 10,
    "empty": false
  },
  "error": null,
  "timestamp": "2025-07-06T22:53:21.118991"
}
```

- `data.content`: 공휴일 정보 목록 (페이징된 결과)
- `types`: 공휴일 유형 (예: `Public`, `Bank`, `School`, ...)
- `pageable`, `totalElements`, `totalPages` 등은 Spring Data JPA의 페이징 정보입니다.

---

## 📌 프로젝트 특징

- ✅ 통일된 응답 포맷 (CommonResponse)
- ✅ RESTful 설계 및 Swagger 문서화
- ✅ 스케줄러 및 자동 동기화를 고려한 설계
- ✅ 예외 처리를 위한 커스텀 Exception 및 ErrorCode 기반 응답

---

## 🧑‍💻 Author

**Inhyeok Kang**  
📧 ggh2260@gmail.com
---
