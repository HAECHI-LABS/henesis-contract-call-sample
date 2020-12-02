# Henesis API Smart Contract Call Example
 이 예제는 Henesis API의 스마트 컨트렉트 호출하기를 사용한 프로젝트입니다.
 
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

- ~infra/EncodeFunction.java 클래스에서 생성된 IERC20.java wrapper code를 사용하여 데이터를 인코딩합니다.
```java
public String getHexTokenData(ContractCallRequestBody contractCallRequestBody) {
  return this.ierc20.transfer(contractCallReqestBody.getToAddress(), new BigInteger(contractCallRequestBody.getValue().substring(2), 16)).encodeFunctionCall();
}
```

- getHexTokenData()에서 IERC20의 transfer 함수를 사용하였고, 인자로 toAddress, value(보낼 토큰의 양)을 입력합니다.
- 해당 함수를 encodeFunctionCall()을 사용하여 hexString 형태로 인코딩합니다.
- 이 데이터를 Henesis 마스터 지갑/사용자 지갑에서 스마트 컨트렉트 호출하기 API의 Data의 필드에 적용합니다.
    
### 실행

#### 마스터 지갑에서 스마트 컨트랙트 호출하기
```json
//Path Variable
blockchainSymbol: eth
masterWalletId: 3c399fee47be3793c2df3516d11232b3

//Request Body
{
  "toAddress": "0x13da3a8be6cc271291515dfb65bd2e8ac73175b4",
  "value": "0x2386f26fc10000"
}
```
- 결과
```shell script
//result
{
  "success": true,
  "code": 0,
  "msg": "Master wallet contract call API"
}

//log
Master wallet contract call has been requested.
Transaction ID: 830de36855e9c73fb5d7b34f597c43cc
Status: REQUESTED
```

#### 사용자 지갑에서 스마트 컨트랙트 호출하기
```shell script
//Path Variable
blockchainSymbol: eth
masterWalletId: 3c399fee47be3793c2df3516d11232b3
userWalletId: a1fec097b9cff8c4dab80985797dc7fc

//Request Body
{
  "toAddress": "0x13da3a8be6cc271291515dfb65bd2e8ac73175b4",
  "value": "0x2386f26fc10000"
}
```
- 결과
```json
//result
{
  "success": true,
  "code": 0,
  "msg": "Master wallet contract call API"
}

//log
User wallet contract call has been requested.
Transaction ID: 131d5f15ee002a264a261787ce936fc0
Status: REQUESTED
```