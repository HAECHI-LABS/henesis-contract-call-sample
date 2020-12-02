# Henesis API Smart Contract Call Example
 이 프로젝트는 스마트 컨트랙트를 호출하기 위해 필요한 Data 필드를 생성하는 법을 설명드리기 위해,
 예시로 Henesis API의 마스터 지갑에서 스마트 컨트렉트 호출하기 API를 이용하여 작성하였습니다.
 
## Requirements
- Docker
- docker-compose
- JDK >= 8
- solidity >= 6

## 설치 방법

- 본 repository를 clone합니다.
- root 디렉토리에서 다음과 같은 명령어를 입력합니다.

 ```
 ./gradlew bootjar
 ```
 - 입력 후, ~/environments 디렉터리에 contract-call-example.jar 파일이 생성되었는지 확인합니다.
- docker-compose:
    - local에서 3000, 8080 포트가 사용중이면 해당 포트를 종료하거나, docker-compose.yaml과 ~/resource/application.yaml 에서 실행 포트를 변경합니다.
    - 터미널에서 다음과 같은 명령어를 입력합니다.

    ```
    ➜ henesis-contract-call-sample/environments docker-compose up -d
    Starting enclave  ... done
    Starting contract-call-example ... done
    ```

- [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/api/swagger-ui.html) 로 접속하여 각 API를 테스트합니다.
- 로그를 확인하려면 위 docker-compose.yaml 에 기입한 경로(저장 경로)에서 example_log.log 파일을 확인하거나, 다음과 같은 명령어로 터미널에서 확인합니다.

    ```
    ➜  henesis-contract-call-sample/environments    docker ps
    CONTAINER ID        IMAGE                       COMMAND                  CREATED             STATUS              PORTS                    NAMES
    e5916a9b44bd        contract-call-example       "java -Djava.securit…"   20 minutes ago      Up 20 minutes       0.0.0.0:8080->8080/tcp   henesis-sample
    f2465b4c089a        haechi/sdk-enclave:stable   "docker-entrypoint.s…"   20 minutes ago      Up 20 minutes       0.0.0.0:3000->3000/tcp   enclave

    ➜  henesis-contract-call-sample/environments    docker logs -f e5916a9b44bd
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
## ERC20 토큰 스마트 컨트랙트 호출로 출금하기
- 스마트 콘트랙트를 호출을 위한 function은 solidity를 활용하여 생성한 wrapper code에서 확인하실 수 있습니다.
- 자신에게 필요한 solidity를 직접 구축하거나, 오픈 소스를 활용하세요.(ex. OpenZeppelin, etc.)
- 본 예제에서는 OpenZeppelin(https://github.com/OpenZeppelin/openzeppelin-contracts) solidity 파일을 사용하였으며, 예제에 필요한 function은 미리 생성하였습니다.
    - 명령어의 옵션 등, 보다 자세한 사항은 Web3j Docs(https://docs.web3j.io/getting_started/deploy_interact_smart_contracts/)에서 확인하실 수 있습니다.
    - solidity 파일로 wrapper code 생성하기:
```shell
➜  henesis-contract-call-sample/contracts   solc IERC20.sol --bin --abi --optimize -o .
➜  henesis-contract-call-sample/contracts   web3j solidity generate --binFile=IERC20.bin --abiFile=IERC20.abi -o . -p erc
```
- IERC20 정의 및 Bean 등록은 config/Config에 작성하였습니다.
- ~/ContractCallService.java 클래스에서 생성된 IERC20.java wrapper code를 사용하여 데이터를 인코딩합니다.
```java
public String encodeTransfer(ContractCallRequestBody contractCallRequestBody) {
  return this.ierc20.transfer(contractCallReqestBody.getToAddress(), new BigInteger(contractCallRequestBody.getValue().substring(2), 16)).encodeFunctionCall();
}
```
- ContractCallService.contractCall()에서 IERC20의 transfer 함수를 사용하였고, wrapper 코드에 정의된 대로 toAddress, value(보낼 토큰의 양)를 인자로 입력합니다.
- 해당 함수를 encodeFunctionCall()을 사용하여 hexString 형태로 인코딩합니다.
- 이 데이터를 Henesis 마스터 지갑/사용자 지갑에서 스마트 컨트렉트 호출하기 API의 Data의 필드에 적용합니다.
    
## 실행

#### 마스터 지갑에서 스마트 컨트랙트 호출하기
```json
//Path Variable
masterWalletId: 3c399fee47be3793c2df3516d11232b3

//Request Body
{
  "toAddress": "0x13da3a8be6cc271291515dfb65bd2e8ac73175b4",
  "value": "0x2386f26fc10000"
}
```
- 결과
```shell script
{
  "id": "794fb4b6ad12825643eca20c66524318",
  "blockchain": "ETHEREUM",
  "sender": "0x7a9f0fa9f8a35baa187cb1e9bfc71ca587218da1",
  "hash": null,
  "error": null,
  "status": "REQUESTED",
  "fee": null,
  "keyId": "85eecaf04d4b271c3611ca29e6f2ea21",
  "isFeeDelegated": false,
  "estimatedFee": "0xbb23d6ae5a00",
  "createdAt": "1606925663533",
  "signedMultiSigPayload": {
    "signature": "0x471c8cf9b08c23adcc81cc6b35f8b3dec82c97f0e06d5420ceb7ac0d47673e9a1819893c64628fb4b92f32448c418f9bb27a727726a0137f49369b89eb8d90511b",
    "multiSigPayload": {
      "value": "0x0",
      "walletAddress": "0xf35a54d58c54d5cba138b85937d1316035563c66",
      "toAddress": "0x0d4c27c49906208fbd9a9f3a43c63ccbd089f3bf",
      "walletNonce": "0x2396595f00ef7d57db69753636afff91b99df0a5d63e6ec0f9631e1c9380fba4",
      "hexData": "0xa9059cbb00000000000000000000000013da3a8be6cc271291515dfb65bd2e8ac73175b4000000000000000000000000000000000000000000000000002386f26fc10000"
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