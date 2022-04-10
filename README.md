<img width="651" alt="image" src="https://user-images.githubusercontent.com/90208100/162629543-4c044dde-d022-4111-8ca0-fd7e3e0a0c17.png">
<img width="668" alt="image" src="https://user-images.githubusercontent.com/90208100/162629556-e464cb38-8d22-42f7-9cc4-22d5930863e0.png">
<img width="629" alt="image" src="https://user-images.githubusercontent.com/90208100/162629562-3b279dc3-8ca1-4bb5-8f11-c6d0f4012efe.png">
<img width="647" alt="image" src="https://user-images.githubusercontent.com/90208100/162629568-df5bdbc6-1a27-4f59-b4b5-b96eea99bd0b.png">

# parkingLot_TDD
### 스펙 1
(1) 특정 car가 들어올때 Entrance 클래스의 인스턴스 메서드인 scan에 car를 전달 시 해당 car의 number를 반환<br>
(2) A-1에 주차 ->user가 A-1 코드를 가지고 있는 ParkingSpace에 주차, 해당 구역 주차 불가능시 에러 던지기<br>
(3)Exit 클래스의 인스턴스 메서드 pay에서 구현 --> 각 경계값에 대해서 테스트 <br>
(추가) 주차장에서 코드를 이용해 주차구역(parkingSpace)의 정보 얻기, 주차장에서 존재하지 않는 코드로 주차구역 정보 얻는 경우 에러 처리 등의 테스트 거침
<br>

### 스펙 2
동시성 테스트라 보고 차가 동시에 입구에 들어오거나 출구로 나갈때 n개 초과하게 하지 않기 위해 쓰레드 풀을 이용 하여 테스트<br>
--> SimultaneousParkingTest 클래스에서 테스트 <br>
ThreadPool의 최대 thread 갯수를 n개로 제한 후 현재 쓰레드 갯수가(메인, 테스트 쓰레드 제외) n개넘지 않는지 테스트 하는 방식으로 구현
<br>

### 스펙 3
--> 스펙 1의 요금과 마찬가지로 가능한 많은 경계 부분의 테스트를 하려고 하였다
(1) 요금표 변경되로 구현<br>
(2) 경차 시 반값 할인 <br>
(3) 트럭 들어올 시 Entrance의 scan에서 에러 터트림<br>

### 스펙 4
(1) User 클래스에서 Payco 회원 여부 나타내는 필드 추가하여 Payco 회원시 10% 할인<br>
(2) 시간 주차권 구현 --> 1시간 주차권, 2시간 주차권 2가지 종류만 존재 <br>
시간 주차권 정책은 <br> 
무조건 결제시 사용 단 두가지 모두 존재시 2시간 미만 -> 1시간 쿠폰 먼저 사용 <br>
아니면 -> 2시간 쿠폰 먼저 사용
     
## 정리
ParkingLot(주차장) 즉 parkingSpace의 집합인 클래스는 인터페이스로 나타냈고 ParkingSpace 관련 메서드들은 선언만 하고 구현을 하지 않았다.
즉 ParkingLot에 자료를 저장하는 자료 구조는 구현하지 않았으며 해당 메서드 또한 구현이 되어 있지 않다. <br>
따라서 ParkingLot 부분은 test시 mock으로 선언후 when절을 통해 stubbing 하여 테스트 하였다. <br>

최대한 바로 구현 보다는 테스트 코드 작성 후 해당 테스트 처리를 하는 방향으로 진행 하였고 구현이 필요하다 생각 되면 하였다. <br>
