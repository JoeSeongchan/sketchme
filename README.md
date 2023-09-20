화상 캐리커처 플랫폼 Sketch Me
# Link
[Notion](https://www.notion.so/0f2e0b26976d46ad97065bbc6050cd99?pvs=21)

# Tech Stack
- Spring Boot Framework 3.1.2
- Java 17
- Spring Batch 
- JPA, JPQL
- Docker 
- Docker Compose
- Shell Script

# Concept
작가가 고객의 얼굴을 보고 화상으로 캐리커처를 그리는 플랫폼

# 주요 도메인 
- 인증 / 인가
- 갤러리 검색 / 조회 
- 예약 
- 채팅 
- 챗봇
- 화상 회의

# 주요 기능 
- 갤러리 검색, 작가 검색 기능
- 작가에게 예약 걸기 기능
- 예약 수락, 거절 기능
- 사용자 간 채팅 기능
- 챗봇이 예약 알람과, 예약 시간 임박 알람을 보내는 기능 
- 화상 회의실 기능
- 회의실 안에서 멀티 레이어로 실시간으로 그림을 그릴 수 있는 기능
- 그림을 그리는 과정을 타임랩스 GIF로 만들어 다운로드할 수 있고, Public하게 공개할 수 있는 기능

# 기술 스택 세부 사항
| 기능             | 활용한 기술 스택       |
| ---------------- | ---------------------- |
| 화상 회의실 기능 | OpenVidu |
| 챗봇             | Spring Batch           |
| 채팅             | Web Socket, Kafka      |
| CICD             | Jenkins                |
| Infra            | Docker, Docker Compose, Shell script                       |

# 시연
![](attachments/스케치1.gif)
![](attachments/스케치2.gif)

# 화면
[Click](화면.md)

# 아키텍처
![](attachments/Pasted%20image%2020230921004011.png)

# 역할
| 이름   | 개발 분야  | 담당 파트                                      |      기타 역할           |
| ------ | ---------- | ---------------------------------------------- | --------------- |
| 김소희 | 프론트엔드 | OpenVidu 화상 회의                             | 팀장            |
| 김영석 | 프론트엔드 | 전체적인 UI, UX 담당                           | 프론트엔드 리더 |
| 박지원 | 프론트엔드 | 채팅 (웹소켓 통신)                             |                 |
| 강병선 | 백엔드     | 채팅 (Kafka, 웹소켓 통신)                      |                 |
| 조성찬 | 백엔드     | OpenVidu API 서버 개발 및 CICD 파이프라인 구축 |인프라, CICD                 |
| 허유정 | 백엔드     | 유저 인증 / 인가 및 전체적인 API 개발          |                 |

# 앞으로의 계획
| 할 일                                                                  | 실행 일자   | 담당 인원      |
| ---------------------------------------------------------------------- | ----------- | -------------- |
| Port And Adapter (헥사고날 아키텍처)로 리팩토링                        | 9/28 ~ 10/4 | 강병선, 조성찬 |
| Domain, Application 계층을 테스트하는 코드 작성                        | 10/5 ~ 11/5 | 강병선, 조성찬 |
| Jenkins 프리 스타일 프로젝트를 Pipeline 코드로 변경 (Pipeline As Code) | 11/6~11/13  | 조성찬               |



# 포팅 방법
아직 가이드되어 있지 않습니다. 리팩토링이 끝난 후 10/4에 업로드될 예정입니다.

# Wiki
## 조성찬
- [Docker Network란?](https://seongchancho.notion.site/Docker-network-67abcbd04cc94911b3256340dfb060e5?pvs=4)
- [Web hook이란?](https://seongchancho.notion.site/Webhook-9120444731534229baf3980c51906e79?pvs=4)
- [DI와 DIP, 그리고 IOC](https://seongchancho.notion.site/DI-IOC-DIP-6fdfee5909d0455cb48b61bba21351e4?pvs=4)
- [JPA, QueryDSL 전략](https://seongchancho.notion.site/JPA-QueryDSL-d5237afe4558469db1f74e6fe0fc834e?pvs=4)
- [중복제거의 위험성](https://seongchancho.notion.site/12cca9620d85451d9dfb11b63a8fd1e3?pvs=4)
- [HTTP와 HTTPS](https://seongchancho.notion.site/HTTP-HTTPS-89e459042c7443578aabe18e87e3cdfb?pvs=4)


