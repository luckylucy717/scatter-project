# scatter-project
Task - 뿌리기 / 받기 / 조회 API

1. 뿌리기 POST/scatter/money/scatter
- header: X-ROOM-ID, X-USER-ID
- param: total_cost(전체금액), member_cnt(멤버 수)

-> 토큰은 세자리로 생성
-> 전체금액 / 멤버 수 만큼 금액을 나누고, 나머지 금액은 한 곳에 더해서 저장
-> 저장시 토큰, room-id, 뿌린 사용자의 id, 전체금액, 나눠진 금액을 각각 저장 


2. 받기 POST /scatter/money/receive
- header: X-ROOM-ID, X-USER-ID
- param : token 

-> 받기를 할 수 있는 리스트를 조회 -> 결과값이 있다면 receive_user_id에 해당 사용자의 아이디로 업데이트
-> 받기를 할 수 있는 리스트가 없을 경우 400으로 리턴 

3. 조회 GET /history 
- header: X-ROOM-ID, X-USER-ID
- param : token 

-> room_id, 뿌린 사용자, 7일 이전을 체크해서 리스트가 있으면 조회 
-> 없을 경우 400으로 리턴
