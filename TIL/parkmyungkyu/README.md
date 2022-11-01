# TIL

## 2022.08.29

### MapReduce Framework

- 맵리듀스 프레임워크에서는 각각의 레코드 또는 튜플은 (KEY, VALUE) 쌍으로 표현됩니다.
- 맵리듀스 프레임워크의 주요 구성 요소는 MapReduce와 HDFS라 불리는 Hadoop 분산 파일 시스템입니다.
- 맵리듀스는 소프트웨어의 수행을 분산하고 HDFS는 데이터를 분산하는 역할을 합니다.

### 맵 페이즈

- 텍스트 무서의 각 라인 마다 map 함수가 호출되고 문서를 스캔하면서 각 단어마다 그 단어를 KEY로 하고 값 1을 VALUE로 해서 (KEY,VALUE) 쌍을 출력합니다.
- map 함수가 다 끝나고 나면 출력한 (KEY,VALUE) 쌍의 KEY에 따라서 해시 함수 같은 것을 이용해서 여러 머신에 분산하여 보냅니다.
- 여기서 같은 KEY를 가진 (KEY,VALUE) 쌍은 반드시 같은 머신으로 보내집니다.

![image](/uploads/9f1bc40a4db97ed1c6e8394ad0b5f089/image.png)

## 2022.08.30

### 셔플링 페이즈

- 맵 페이즈가 끝나면 각 머신마다 그 머신으로 전달된 Map 함수들이 출력한 (KEY,VALUE) 쌍을 KEY를 이용하여 Sorting을 하고 난 후에 각각의 KEY마다 그 KEY를 가진 VALUE들을 모아서 VALUE LIST를 만들고 (KEY, VALUE-LIST) 형태를 생성한다.

![image](/uploads/bb58f5bf7f63665e8242172ab7935574/image.png)

### 리듀스 페이즈

- 셔플링 페이즈가 끝나면 각 머신 마다 각각의 (KEY, VALUE-LIST) 쌍에 대해서 각각의 KEY마다 Reduce 함수가 호출됩니다.

![image](/uploads/4e3ab9cee0fc1a21d8e550495ca5ee23/image.png)

- 각각의 Reduce 함수는 입력으로 주어진 (KEY,VALUE-LIST) 쌍에 대해서 VALUE-LIST에 있는 값들을 다 합한 값을 VALUE로 만들어서 (KEY,VALUE) 형태로 출력합니다.

### Word count

#### mapper class

![image](/uploads/fb6a825ca47e329ce874637f2a228a32/image.png)

#### reducer class

![image](/uploads/c3c8cbee6d309c4cf7434a169d958f0f/image.png)

## 2022.08.31

## Inverted index

아래와 같은 5개의 문서가 있을 때에 inverted index는 각 단어 마다 그 단어가 나타나는 문서 아이디 doc와 그 문서에서 나오는 위치 position을 doc:position 형태로 아래와 같은 리스트를 만듭니다.

![image](/uploads/c80e637482b13f9c741b877ed01bf309/image.png)

### 인버티드 인덱스 생성 맵리듀스 알고리즘

- 각 머신에서 각 문서마다 Map 함수가 호출되고 문서를 스캔하면서 단어 하나마다 그 단어를 KEY로 하고 문서 아이디 DOC와 그 문서에서 나오는 위치 POSITION 을 DOC:POSITION 형태로 VALUE으로 만들어 (KEY,VALUE) 로 출력합니다.

![image](/uploads/ffcd35ed5e52af679546b2828c9c25c5/image.png)

- 셔플링 페이즈에서는 Map 함수가 출력한 (KEY,VALUE) 쌍에 대해서 같은 KEY를 가진 VALUE들의 리스트를 모은 뒤에 VALUE-LIST를 만들고 (KEY,VALUE-LIST) 쌍을 출력합니다.

![image](/uploads/6967ced2f793554615485199e319f473/image.png)

- 셔플링 페이즈가 끝나면 각각의 (KEY,VALUE-LIST) 쌍에 대해서 각각의 KEY마다 Reduce 함수가 호출되는데 VALUE-LIST에 있는 VALUE들을 다 합한 값을 (KEY,VALUE 형태로 출력합니다.)

![image](/uploads/45e7a5d67acc65354df73e6aee2abf30/image.png)

### code

#### mapper class

![image](/uploads/ecd097c2c6f60245c18893b7fb286d8d/image.png)

#### reducer class

![image](/uploads/a7eaa2acd026a9edc789fa06658a3f6e/image.png)
