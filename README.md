# 프로젝트 아키텍처
<img width="500" height="700" alt="image" src="https://github.com/user-attachments/assets/27ceb443-7077-44d4-bd8f-ab2cebdfb2d6" />


**[구현한 기능]**
- ## 회원가입 API → 포인트 적립
   - 게시글 서비스에서 사용해야 하는 사용자 데이터(user_id, name)를 동기화하기 위해서, 사용자 서비스에서 user_id, name의 데이터가 생성/변경/삭제가 될 때마다 카프카로 이벤트를 발행해주어야 한다.
   - 회원가입 API에서 "**게시글 조회 API 최적화를 위해서**" 사용자 서비스에 있는 유저 테이블과 게시글 서비스의 데이터 동기화를 할려고 회원가입 API에 카프카로 이벤트 발행을 하였다.

    <img width="700" height="580" alt="image" src="https://github.com/user-attachments/assets/842feae4-2774-447a-90d9-0f7554fd9acc" />

  ### 게시글 조회 API 최적화
  <img width="700" height="642" alt="image" src="https://github.com/user-attachments/assets/a0185fe0-2a19-44e3-b169-cd68bc79fbce" />
  
  - ‘게시글 조회 로직’을 구현할 때 위의 방식처럼 구현했었다. 이 방식은 게시글 조회 로직의 트래픽이 점차 증가하면서 사용자 서비스의 ‘사용자 DB’에 부담을 줄 수 있다. 또한 ‘게시글 조회 로직’이 더 복잡해져서 사용자 서비스 뿐만 아니라 여러 서비스에 데이터를 조회해서 사용해야 한다면 로직이 복잡해진다.
  - 이런 문제가 발생한다면 게시글 조회 로직을 최적화 할 필요가 있다. 조회 로직의 가장 간단한 최적화 방법은 ‘데이터 동기화’이다. 
   
  <img width="700" height="1506" alt="image" src="https://github.com/user-attachments/assets/63cc511f-7190-4f5f-b6ea-448428255627" />
  
     - 게시글 서비스에서 게시글 조회를 할 때 사용자 서비스의 사용자 정보(`user_id`, `name`)를 같이 조회한다. 즉, 게시글 서비스는 사용자 정보(`user_id`, `name`)가 필요하다. 이걸 해결하기 위해 게시글 서비스에서 필요한 사용자 정보를 관리할 수 있는 테이블을 별도로 두는 것이다. 
     - 그러고 사용자 DB에서 데이터 변경이 일어날 때마다 카프카로 ‘사용자 정보 변경 메시지’를 발행한다. 그럼 게시글 서비스가 해당 메시지를 구독하고 있다가, 사용자 정보가 변경될 때마다 게시글 DB에 사용자 정보를 같이 동기화시켜주는 방식이다. 
     - 이런 식으로 구성하게 되면 게시글 서비스가 자체적으로 사용자 정보(`user_id`, `name`)을 가지고 있기 때문에, 게시글 정보를 조회할 때 사용자 서비스에 데이터를 따로 요청하지 않아도 된다. 그래서 사용자 DB에 부담을 주지 않게 되고, 게시글 조회 로직도 훨씬 간단하게 바뀌게 된다.

---
- ### 로그인 API → JWT 토큰 발급

  - 각 서비스마다 인증이 필요한 API를 가지고 있다면, 각 서비스에 인증 로직을 전부 구현해야 한다. 구체적으로 예를 들자면, 인증을 JWT로 한다고 했을 때 모든 서비스에 JWT 토큰을 검증하는 로직을 추가해야 한다.
  - 하지만 중복된 코드가 발생하기 때문에 유지보수가 힘들어진다. 이 때문에 아래와 같이 API Gateway에만 인증 로직을 구현한다. 
  <img width="700" height="790" alt="image" src="https://github.com/user-attachments/assets/04312a1d-f0c4-4b94-99b5-ac54e27ad504" />
---

- ### 게시글 작성 API (JWT 토큰 검증) → 포인트 차감 → 활동 점수 적립
  - 사용자가 게시글 작성 API에 요청을 보냈을 때, ‘포인트 차감 → 게시글 작성’ 로직까지만 확실하게 작동한 걸 확인하고 성공 응답을 내려줘도 된다. ‘활동 점수 적립’ 처리가 끝날 때까지 기다렸다가 응답해줄 필요는 없다.
  - 즉, ‘활동 점수 적립’은 메시징 기반의 비동기 방식으로 처리해도 된다.
  - 사용자 서비스가 BoardCreated 이벤트를 구독한다는 건, Kafka의 board-created토픽을 소비하는 Consumer로 등록돼서 게시글 생성 이벤트가 발생할 때마다 자동으로 처리한다는 뜻입니다.
  <img width="800" height="998" alt="image" src="https://github.com/user-attachments/assets/34fc96a2-6fa8-4595-ba8c-07166b68836a" />
---
- 전체 게시글 조회 API → 사용자 정보도 같이 조회
- 특정 게시글 조회 API → 사용자 정보도 같이 조회
---


## 프로젝트의 모든 API 잘 작동하는 지 테스트하기
### 테스트하기

> 실제 클라이언트가 요청을 보내듯이 API Gateway 주소(localhost:8000)로 테스트
> 
1. **테스트를 위해 DB 비우기**
    
    **[board-db]**
    
    `boards`, `users` 테이블 데이터 전부 삭제하기
    
    **[point-db]**
    
    `points` 테이블 데이터 전부 삭제하기
    
    **[user-db]**
    
    `users` 테이블 데이터 전부 삭제하기
    
2. **회원가입 API 테스트하기**
    1. **API 요청 보내기**
        
        <img width="700" height="748" alt="image" src="https://github.com/user-attachments/assets/1404ca53-a220-4196-8b55-b8f43f5c0d1c" />

        
    2. **DB 확인하기**
        
        **[user-db]**
        
        <img width="700" height="288" alt="image" src="https://github.com/user-attachments/assets/20151777-c2cb-4e28-a17d-0fac31d97510" />

        
        **[point-db]**
        
        <img width="700" height="253" alt="image" src="https://github.com/user-attachments/assets/60ce4705-8f99-45f1-a713-1cd4e14aee6a" />

        
        **[board-db]**
        
        <img width="700" height="280" alt="image" src="https://github.com/user-attachments/assets/0a457db2-db53-4714-9c75-296475a18ac7" />

        
3. **로그인 API 테스트하기**
    
    <img width="700" height="795" alt="image" src="https://github.com/user-attachments/assets/21d67378-6465-40a3-b72a-8a03972eec0b" />

    

로그인해서 얻은 토큰을 잘 복사해두기

1. **게시글 작성 API 테스트하기**
    1. **API 요청 보내기**
        
        <img width="500" height="700" alt="image" src="https://github.com/user-attachments/assets/4b0b5771-4985-4d96-994a-25645827f23f" />

        <img width="500" height="700" alt="image" src="https://github.com/user-attachments/assets/c7bf77b6-d698-4ffe-8147-ce2dab3113db" />

        
    2. **DB 확인하기**
        
        **[board-db]**
        
        <img width="700" height="268" alt="image" src="https://github.com/user-attachments/assets/97c8f908-53f9-4727-bceb-9f82b702ec24" />

        
        **[user-db]**
        
        <img width="700" height="250" alt="image" src="https://github.com/user-attachments/assets/2668d771-267a-433c-983b-251c1f809dc1" />

        
        **[point-db]**
        
        <img width="700" height="243" alt="image" src="https://github.com/user-attachments/assets/552da7d9-55d7-4c55-b16a-0d13f951c803" />

        
2. **게시글 조회 API 테스트**
    
    **[전체 게시글 조회 API]**
    
    <img width="700" height="993" alt="image" src="https://github.com/user-attachments/assets/5de6221e-1c82-41d7-885f-84ba0dec9d19" />

    
    **[특정 게시글 조회 API]**
    
    <img width="700" height="955" alt="image" src="https://github.com/user-attachments/assets/f499b113-aa30-4d16-8b1d-41f038ea7781" />
