# eGovframe

## **의존성 설정**

프로젝트의 `pom.xml` 파일에 다음과 같은 의존성을 추가한다.

### **MariaDB Java Client**

MariaDB 데이터베이스에 연결하기 위한 JDBC 드라이버이다.

```
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>3.0.10</version>
</dependency>
```

### **Apache Commons DBCP**

데이터베이스 연결 풀링을 제공하는 라이브러리이다.

```
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
    <version>2.9.0</version>
</dependency>
```

## **MyBatis 설정**

MyBatis 매퍼를 설정하기 위해 `MapperConfigurer`를 사용한다. `MapperConfigurer`는 특정 패키지에서 `@Mapper` 어노테이션을 찾아 MyBatis 매퍼로 등록한다.

MapperConfigurer 설정

```
<bean class="org.egovframe.rte.psl.dataaccess.mapper.MapperConfigurer">
    <property name="basePackage" value="**.**.dao" />
</bean>
```

## **요약**

- **의존성**: `mariadb-java-client`와 `commons-dbcp2`를 사용하여 데이터베이스 연결 및 연결 풀링을 설정한다.
- **MyBatis 설정**: `MapperConfigurer`를 사용하여 특정 패키지(`*.**.dao`) 내의 `@Mapper` 어노테이션이 있는 인터페이스를 매퍼로 등록한다.

# **Enterprise Architecture**

### **Presentation Layer**

- 사용자의 요청을 받거나 결과를 보여주는 역할을 한다.
- 웹 애플리케이션 및 모바일 애플리케이션이 포함된다.
- 주요 구성 요소
    - **Controller**: 사용자의 요청을 처리하고, 서비스 계층에 데이터베이스 작업을 요청한다.
    - **View**: 사용자에게 결과를 보여준다.
    - **Dispatcher Servlet**: 요청을 적절한 컨트롤러에 매핑한다.

### **Business Layer**

- 데이터 검증 및 비즈니스 로직을 처리한다.
- 서비스 인터페이스를 통해 기능적 명세를 제공한다.
- 유효성 검사
- 주요 구성 요소
    - **Service Interface**: 데이터베이스 접근을 위한 메서드를 정의한다.
    - **Service Implementation**: 서비스 인터페이스를 구현하여 비즈니스 로직을 처리한다.

### **Data Access Layer**

- 데이터베이스와의 CRUD 작업을 담당한다.
- 주요 구성 요소
    - **DAO (Data Access Object) Interface**: 데이터베이스 작업을 위한 메서드를 정의한다.
    - **DAO Implementation**: DAO 인터페이스를 구현하여 실제 데이터베이스와 상호작용한다.

### **데이터 흐름**

1. **Controller에서 Service Interface 함수 호출**: Controller는 사용자의 요청을 처리하기 위해 Service Interface의 메서드를 호출한다.
2. **Service Interface 구현체 호출**: Service Interface를 구현한 Service Implementation 클래스의 메서드가 호출된다.
3. **Service Implementation에서 DAO Interface 함수 호출**: Service Implementation 클래스는 DAO Interface의 메서드를 호출한다.
4. **DAO Interface 구현체를 통해 데이터베이스 접근**: DAO Interface를 구현한 DAO Implementation 클래스가 데이터베이스에 접근하여 필요한 작업을 수행한다.

각 계층 간의 함수 호출은 VO(Value Object) 또는 DTO(Data Transfer Object)를 통해 데이터를 주고받다. 이는 데이터베이스에서 데이터를 조회하거나, 사용자에게 데이터를 전달하는 데 사용된다.

**web.xml**

```
    <session-config>
        <session-timeout>30</session-timeout>
    </session-config>
```

30분동안 로그인 유지.

### **프로시저 사용하는 이유**

- 네트워크 관점: 트래픽 비용 절감
- 설계 관점: 로직 분리해서 유지보수성과 분업측면에서 좋음

**Q:실무에서 DBLINK를 왜 사용함?**

**A:**

**Enterprise application integration (EAI)에서**

**DBLINK와 프로시저**를 사용해서 **하나의 트랜잭션**으로 생성한다.

이를 통해 A업체 시스템에서 B업체(또는 정부기관) 시스템으로,

B업체시스템에서 C업체 시스템으로 데이터교환을 하게된다.
