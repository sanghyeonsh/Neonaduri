#TIL

### 2022.08.29

# 하둡 이론 정리

# 하둡 기본

- Distribute Programming Framework
- HDFS(분산저장) + MapReduce(분산처리)

## HDFS

- NameNode
- Secondary NameNode
- DataNode

## MapReduce

- Job Tracker
- Task Tracker

# 하둡 이론

(1) 분산 프로그래밍이란?

⇒ 분산 컴퓨팅이라고도 하며 인터넷에 연결된 여러 컴퓨터들의 처리 능력을 이용하여 거대한 계산 문제를 해결하려는 분산처리 모델

(2) 하둡이란?

⇒ 분산 프로그래밍 프레임워크로 HDFS와 MapReduce로 구성되어 있으며 ‘Scale out’, ‘Master-Slave’, ‘Logic이동’ 등의 특징을 가진다. 가장 큰 약점은 Job Tracker가 죽으면 전체가 돌아가지 않는 약점이 있음

1. Scale out : 여러개를 추가
2. Master-Slave : 마스터 죽으면 끝
3. Logic 이동 : 데이터가 이동하지 않고 여러곳에 저장하고 그 곳에 가서 분석함

(3) HDFS란?

⇒ Hadoop Distributed File System의 약자로, 대용량 파일을 분산된 서버에 저장하고, 그 데이터를 빠르게 처리할 수 있게 하는 파일 시스템. 네임노드(마스터)와 데이터노드(슬레이브)로 구현되어있음

네임노드의 기능

1. 메타데이터 관리 : 파일 시스템을 유지하기 위한 메타데이터를 관리
2. 데이터노드 모니터링 : 데이터노드는 네임노드에게 3초마다 하트비트를 전송. 네임노드는 이를 이용하여 데이터노드의 실행상태와 용량을 체크함
3. 블록관리 : 장애가 발생한 데이터노드의 블록을 새로운 데이터노드에 복제함. 용량이 부족하면 여유가 있는 데이터노드에 블록을 옮김
4. 클라이언트 요청 접수 : 클라이언트가 HDFS에 접근하려면 반드시 네임노드에 먼저 접속해야 함. HDFS에 파일을 저장할 경우 기존 파일의 저장여부와 권한 확인 절차를 거쳐 저장을 승인함

(4) MapReduce란?

⇒ 대용량 데이터 처리를 위한 분산 프로그래밍 모델로 흩어져 있는 데이터를 수직화하여 그 데이터를 각각의 종류별로 모으고(Map), Filtering과 Sorting을 거쳐 데이터를 뽑아내는(Reduce) 분산처리 기술로 구성되어 있음

(5) 여러 가지 용어들

- Map : 데이터 변형, 값들을 모두 key와 value로 만듦
- shuffle : 중간단계 시스템에서 알아서 map과 reduce만 개발자가 조절할 수 있음
- Reduce : 결과를 집계, 각 key와 value를 중복에 대해 count함 출력파일을 만듦

### 2022.08.30

# 맵리듀스 자세히 알아보기

# 맵리듀스 기본

- 맵리듀스는 여러 노드에 태스크를 분배하는 방법으로 각 노드 프로세스 데이터는 가능한 경우, 해당 노드에 저장됨
- 맵리듀스 태스크는 맵과 리듀스 총 두 단계로 구성됨
- fork-join으로 예를 들면 큰 작업에 대해 fork로 분할하고 작은 작업을 다시 join하게 됨. 하둡에서는 큰 데이터가 들어왔을 때 64MB단위 블럭으로 분할하여 각각 블럭에 대한 연산을 함

⇒ 즉, 하둡에서는 계산 시 큰 파일을 블럭단위로 나누고 모든 블럭은 같은 Map작업을 수행하고 이후 Reduce작업을 수행하게 됨

## 맵리듀스(MapReduce)

- MapReduce는 하둡 클러스터의 데이터를 처리하기 위한 시스템으로 Map과 Reduce사이에는 shuffle과 sort라는 스테이지가 존재함.
- 각 Map task는 전체 데이터 셋에 대해서 별개의 부분에 대한 작업을 수행하게 되는데, 기본적으로 하나의 HDFS block을 대상으로 수행하게 됨.
- 모든 Map태스크가 종료되면, MapReduce시스템은 intermediate데이터를 Reduce phase를 수행할 노드로 분산하여 전송함

⇒ Distributed File System에서 수행되는 MapReduce작업이 끝나면 HDFS에 파일이 써지고 MapReduce작업이 시작할 때는 HDFS로부터 파일을 가져오는 작업이 수행됨

## 잡트래커(JobTracker)

- 맵리듀스 Job들은 JobTracker라는 소프트웨어 데몬에 의해 제어됨
- JobTracker들의 역할 : 클라이언트가 맵리듀스 Job을 JobTracker에게 보내면 Jobtracker는 클러스터의 다른 노드들에게 맵과 리듀스 Task를 할당함. 이 노드들은 TaskTracker라는 소프트웨어 데몬에 의해 실행되고 실제로 맵 또는 리듀스 Task를 인스턴스화하고 진행 상황을 JobTracker에게 보고할 책임이 있음
- Job은 전체 프로그램을 의미하고 데이터 집합을 통해 매퍼와 리듀서를 전체 실행함
- Task는 데이터 조각을 통해 하나의 매퍼 또는 리듀서를 실행함

## 매퍼(Mapper)

- 하둡은 네트워크 트래픽을 방지하기 위해 메타 데이터의 일부분을 가지고 노드에서 처리함. 동시에 실행되는 여러 매퍼는 각각 입력 데이터의 일부를 처리하는 단계를 포함
- 각각의 map, reduce를 어떤 PC에서 수행할지는 Job Tracker에서 결정하고 Mapper에서 중간 결과가 나오면 다시 그 중간 결과를 Shuffling하고 그 결과를 Reducer에게 전달함
- Reducer가 실제 어떤 상황에서 사용되는지 예를 들면 회사에서 어떤 데이터에 대한 월별 통계를 내고 싶을 때 reducer의 결과로 12개의 결과가 나오면 각각의 reducer 결과는 각 월별 통계 자료를 갖고 있게 되는 것. 만약 요일별 통계를 내고 싶다면 총 7개의 결과만 나오면 됨. 즉 Mapper가 수집한 결과를 Shuffling하여 우리가 원하는 결과를 얻어내는 과정

## 리듀서(Reducer)

- 맵 단계가 끝나면, 중간 단계의 키 값을 기반으로 중간 값을 리스트 형태로 조합하게 됨. 그 뒤 리스트는 리듀서로 전달이 되는데 수행 절차는 아래와 같음
- 하나의 리듀서나 여러개의 리듀서가 존재할 때 중간 키와 연관되어 있는 모든 값은 같은 리듀서로 보내짐
- 중간 키와 그 값들의 리스트들은 키 순서대로 정렬되어 리듀서로 보내짐 이 단계를 셔플과 정렬이라고 함

### 2022.08.31

# wordcount sort

### Mapper

![image](/uploads/a9c7fe8774885af8db1b46d297e7f324/image.png)

### Reducer

![image](/uploads/7bc40f8de798b6697b6efb3d74bc6496/image.png)

### 2022.09.02

# all pair partition

### Mapper

![image](/uploads/22b540c6fb3352e5a57234015f9c44fc/image.png)

### Reducer

![image](/uploads/74ca6dddbf1bada17737becec12c6076/image.png)
