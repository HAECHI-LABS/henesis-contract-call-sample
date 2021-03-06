# Henesis API Smart Contract Call Example
 Henesis API를 활용하여 스마트 컨트랙트 함수 호출하는 방법을 설명하는 예제 코드입니다.
 ["마스터 지갑에서 스마트 컨트랙트 호출하기"](https://docs.henesis.io/integrate-with-api/api-list/v2/eth-klay/master-wallet/send-transaction-from-master-wallet)를 사용하여 스마트 컨트랙트 함수를 호출합니다.
 
## Requirements
테스트를 하기 위해 아래와 같은 환경이 설치되어 있어야합니다.
- Docker
- docker-compose
- JDK >= 8
- Web3J
    * 설치방법(linux/macOS): 
    ```shell
    $ curl -L get.web3j.io | sh
    ```

## 실행 방법
1. 소스코드 다운 받기
```
git clone https://github.com/HAECHI-LABS/henesis-contract-call-sample.git
```
2. JAR 이미지 생성
- 샘플 디렉토리로 이동하여 JAR 파일을 생성합니다.
- 입력 후, `environments` 디렉터리에 contract-call-example.jar 파일이 생성되었는지 확인합니다.
```
$ cd henesis-contract-call-sample
$ ./gradlew bootjar
```
3. docker-compose 실행
- `environments` 폴더로 이동하여 doecker 
```
$ cd henesis-contract-call-sample/environments
$ docker-compose up -d
Starting enclave  ... done
Starting contract-call-example ... done
```
4. 실행 확인
- `docker ps` 명령어를 통해 올바르게 서버가 실행되고 있는지 확인합니다. 
 ```
$ docker ps
CONTAINER ID        IMAGE                       COMMAND                  CREATED             STATUS              PORTS                    NAMES
e5916a9b44bd        contract-call-example       "java -Djava.securit…"   20 minutes ago      Up 20 minutes       0.0.0.0:8080->8080/tcp   henesis-sample
f2465b4c089a        haechi/sdk-enclave:stable   "docker-entrypoint.s…"   20 minutes ago      Up 20 minutes       0.0.0.0:3000->3000/tcp   enclave
```

## 스마트 컨트랙트 함수 호출 (ex. ERC20 출금)
1. Wrapper 클래스 생성하기(사전준비: ABI파일)
- Wrapper 클래스는 abi 파일을 자바에서 상호작용할 수 있게 ABI의 함수들을 담아 만들어진 자바 클래스입니다.
- Contract call을 위해 필요한 데이터는 함수를 만들어 인코딩해야합니다.
- "-o | --option, -p | --package" 옵션을 통해 원하는 위치에 wrapper 클래스를 생성할 수 있습니다. 입력한 위치의 패키지에 abi 파일과 동일한 이름의 java 파일이 생성된 것을 확인하실 수 있습니다.
```shell
$ cd henesis-contract-call-sample/contracts
$ web3j solidity generate --abiFile=IERC20.abi -o ../src/main/java/ -p io.haechi.henesis.example.contracts
$ cd ../src/main/java/io/haechi/henesis/example/contracts/
$ ls -l
IERC20.java
```
- 파일 구조
```shell
henesis-contract-call-sample:
   build
      contracts:
         IERC20.abi
      ...
      src:
         main:
             java:
                 io.haechi.henesis.example:
                 contracts:
                     IERC20.java
                     
```
2. 인스턴스 생성하기
- 함수를 사용하기 전에 아래와 같이 해당 인스턴스를 load 후 사용해야합니다.
- IERC20.load의 파라미터
     * param1 - 배포한 토큰 스마트 컨트랙트의 주소를 작성합니다.
     * param2 - Web3J
     * param3 - dummy keypair을 생성하고, 이를 이용해 Credentials를 생성합니다.
     * param4 - gasPrice와 gasLimit을 가지고 오기 위해 Web3j에서 제공하는 DefaultGasProvider를 사용합니다.
```java
ECKeyPair dummyKeyPair = Keys.createEcKeyPair();
return IERC20.load(
       "0x0d4c27c49906208fbd9a9f3a43c63ccbd089f3bf",
       this.web3j(),
       Credentials.create(dummyKeyPair),
       new DefaultGasProvider()
);
```
3. Data 생성하기
- IERC20.java에서 사용할 함수에 필요한 파라미터를 넘겨줍니다. 해당 함수의 결과를 encodeFunctionCall()를 이용해 인코딩하여 data를 생성합니다.
```java
// function encoding
String data = this.ierc20.transfer(contractCallRequestBody.getToAddress(), new BigInteger(contractCallRequestBody.getValue().substring(2), 16)).encodeFunctionCall();
```
- 인코딩된 데이터와 value, gasPrice 등 다른 파라미터들을 Henesis API에 나온 내용처럼 API를 호출합니다.

4. [Henesis API 호출하기](https://docs.henesis.io/integrate-with-api/api-list/v2/eth-klay/master-wallet/send-transaction-from-master-wallet)
- 위에서 인코딩한 data와 Henesis API에 사용하기 위한 인자들을 body 객체에 담아서 Henesis API를 호출합니다.
```java
// HenesisApiContractCallBody는 위에서 생성한 data와 Henesis API에 입력할 남은 파라미터들을 모두 담은 객체입니다.
public ContractJsonObject sendContractCall(String masterWalletId, HenesisApiContractCallBody data) {
       Map<String, Object> param = new HashMap<>();
       param.put("master_wallet_id", masterWalletId);
       return this.restTemplate.exchange(
              "/eth/wallets/{master_wallet_id}/contract-call",
              HttpMethod.POST,
              new HttpEntity(data),
              ContractJsonObject.class,
              param
       ).getBody();
}
```
    
## 테스트

#### 마스터 지갑에서 스마트 컨트랙트 호출하기
1. 아래와 같이 필요한 파라미터들을 Swagger 등에 입력하여 API 요청을 보냅니다.
- API 테스트를 편리하게 할 수 있도록 Swagger를 연동해뒀습니다. 
- 프로젝트 실행 후 [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html) 로 접속하여 각 API를 테스트할 수 있습니다.
```json
//Path Variable
blockchainSymbol: ETH
masterWalletId: 3c399fee47be3793c2df3516d11232b3

//Request Body
{
  "toAddress": "0xac0ba990ad62804a09ae4e35bdd2f5446e17eff8",
  "value": "0x2386f26fc10000"
}
```
```json
//Path Variable
blockchainSymbol: KLAY
masterWalletId: 72a70046e6991e9e2fb3bf8d274c5191

//Request Body
{
  "toAddress": "0xdac3d690094ec443f81e873d7e6b38a9daae48a7",
  "value": "0x2386f26fc10000"
}
```
- ETH 입력 예시
![스크린샷 2020-12-08 02 09 41](https://user-images.githubusercontent.com/32338616/101381726-745a8180-38fa-11eb-9caf-8c8f755884fe.png)
- KLAY 입력 예시
![스크린샷 2020-12-08 01 57 57](https://user-images.githubusercontent.com/32338616/101381026-96073900-38f9-11eb-9689-2090ae060ae2.png)


2. 결과
- ETH 
```shell script
{
  "id": "0d3333d2d234492d3e4e9ff264e2d838",
  "blockchain": "ETHEREUM",
  "sender": "0x7a9f0fa9f8a35baa187cb1e9bfc71ca587218da1",
  "hash": null,
  "error": null,
  "status": "REQUESTED",
  "fee": null,
  "keyId": "85eecaf04d4b271c3611ca29e6f2ea21",
  "isFeeDelegated": false,
  "estimatedFee": "0x16a9e0b271692",
  "createdAt": "1607360859456",
  "signedMultiSigPayload": {
    "signature": "0x37dba2504d9176dfdb4057831ff297a0baff850484f2a2fc250e4296aa43b834617e0e972e33797788f11c3a718cb4e114e64d6d2a7cf42ea555cf23be0c570d1c",
    "multiSigPayload": {
      "value": "0x0",
      "walletAddress": "0xf35a54d58c54d5cba138b85937d1316035563c66",
      "toAddress": "0x0d4c27c49906208fbd9a9f3a43c63ccbd089f3bf",
      "walletNonce": "0x397d498f4ef97c17a7310c9b2ccf8f935ac38dcd73c575f9c1332c9f2321ac35",
      "hexData": "0xa9059cbb000000000000000000000000ac0ba990ad62804a09ae4e35bdd2f5446e17eff8000000000000000000000000000000000000000000000000002386f26fc10000"
    }
  },
  "rawTransaction": {
    "nonce": null,
    "to": null,
    "value": null,
    "data": null,
    "gasPrice": null,
    "gasLimit": "0xf4240"
  }
}
```
- KLAY 결과
```shell script
{
  "id": "cc35077a562db1d8f9dc2c053d7daebf",
  "blockchain": "KLAYTN",
  "sender": "0x43b8ca494b276ee849166f394ceed53335e0b0d2",
  "hash": null,
  "error": null,
  "status": "REQUESTED",
  "fee": null,
  "keyId": "828e8f4ebad4c504f22403d94e2d6355",
  "isFeeDelegated": false,
  "estimatedFee": "0x9ddbc28e3be00",
  "createdAt": "1607360905458",
  "signedMultiSigPayload": {
    "signature": "0xa429273e74b14f9b27f4a27b96baa9392de890b26bb61d19485034091619c35e1e90efa95de142adbdfc3a6bcc81b2341ebb46c030f0e489d7e82471207f6eee1c",
    "multiSigPayload": {
      "value": "0x0",
      "walletAddress": "0x0167de4a39d9bb248bfb958776b47d9fa6e34060",
      "toAddress": "0x5ff1e48b489c67366f936ed82978e913debd7ad8",
      "walletNonce": "0xee586d905f4e15c7b4e6a33f5e6edad89b547974f02a9984289d54ee1136672f",
      "hexData": "0xa9059cbb000000000000000000000000dac3d690094ec443f81e873d7e6b38a9daae48a7000000000000000000000000000000000000000000000000002386f26fc10000"
    }
  },
  "rawTransaction": {
    "nonce": null,
    "to": null,
    "value": null,
    "data": null,
    "gasPrice": null,
    "gasLimit": "0xf4240"
  }
}
```

## 참고

1. 로그 확인
- 로그를 확인하려면 `docker-compose.yaml` 에 기입한 경로(저장 경로)에서 `example_log.log` 파일을 확인하거나, `docker logs` 명령어로 로그를 출력합니다.
- `docker logs`의 파라미터는 container id로 `docker ps`를 통해 확인할 수 있습니다.
```
$ docker logs -f e5916a9b44bd
2020-11-17 01:57:22.107  INFO 1 --- [           main] o.s.b.SpringApplication                  :
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
 '  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/
:: Spring Boot ::        (v2.3.4.RELEASE)
(생략)
```
