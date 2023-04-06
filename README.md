**스프링부트_코드로배우는.pdf p124~186 2023-03-22**

**스프링부트_코드로배우는.pdf p186~258 2023-03-23**

**스프링부트_코드로배우는.pdf p258~329 2023-03-24**

**스프링부트_코드로배우는.pdf p329~393 2023-03-27 JPA 다대다**

**스프링부트_코드로배우는.pdf p393~472 2023-03-28**

**스프링부트_코드로배우는.pdf p472~523 2023-03-29**

**스프링부트_코드로배우는.pdf p523~560 2023-03-30**

---

### 오류 해결 목록

### `**compile()**` 메서드 오류 : **`implementation()`**또는 **`api()`** 메서드를 사용

### `**querydsl`** 버전 오류 :

스프링 부트 버전이 지속적으로 업데이트 되면서 Querydsl의 버전도 5.0을 권장
참고 : [https://wangtak.tistory.com/m/44](https://wangtak.tistory.com/m/44)

```jsx
buildscript {
   ext {
      queryDslVersion = "5.0.0"
   }
}

plugins {
   id 'org.springframework.boot' version '2.6.8'
   id 'io.spring.dependency-management' version '1.0.11.RELEASE'
   // querydsl 추가
   id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
   id 'java'
}

group = 'com.wangtak'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
   compileOnly {
      extendsFrom annotationProcessor
   }
}

repositories {
   mavenCentral()
}

dependencies {
   implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
   implementation 'org.springframework.boot:spring-boot-starter-web'

   //querydsl 추가
   implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
   annotationProcessor "com.querydsl:querydsl-apt:${queryDslVersion}"

   compileOnly 'org.projectlombok:lombok'
   developmentOnly 'org.springframework.boot:spring-boot-devtools'
   annotationProcessor 'org.projectlombok:lombok'
   testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
   useJUnitPlatform()
}

// querydsl 세팅 시작
def querydslDir = "$buildDir/generated/querydsl"
querydsl {
   jpa = true
   querydslSourcesDir = querydslDir
}
sourceSets {
   main.java.srcDir querydslDir
}
configurations {
   querydsl.extendsFrom compileClasspath
}
compileQuerydsl {
   options.annotationProcessorPath = configurations.querydsl
}
// querydsl 세팅 끝
```

### 모달창 uncaught referenceerror $ is not defined 오류 : 부트스트랩 버전 문제

```jsx
<script th:inline="javascript">
            var msg = [[${msg}]];
        
            console.log(msg);
        
            if(msg) {
                var myModal = new bootstrap.Modal(document.querySelector('.modal'), {
                    keyboard: false
                });
        
                myModal.show();
            }
 </script>
```

코드 변경하고,

```jsx
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script th:src="@{/js/scripts.js}"></script>
```

basic.html에 있는 코드 list.html 최상단으로 옮기기 (안해도됨!)

---

### $ 인식 안되는 문제


```jsx
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
```

위 코드 최상단에 붙여 넣어주니까 작동 됬다

---

- ****[Error - Failed to configure a DataSource 에러](https://7942yongdae.tistory.com/128)****

<aside>
💡 자바 버전이 올라가면서 특정 명령어가 안돌아 간다는 것을 의미한다.

</aside>

### p. 278  `no session` 에러

```jsx
@Override
    public void modify(BoardDTO boardDTO) {
       Board board = repository.getOne(boardDTO.getBno());

       if(board != null) {
           board.changeTitle(boardDTO.getTitle());
           board.changeContent(boardDTO.getContent());

           repository.save(board);
       }
    }
```

스프링 버전 업그레이드로 인한 오류이다 코드 아래로 변경

```jsx
@Override
public void modify(BoardDTO boardDTO) {
   Optional<Board> optionalBoard = repository.findById(boardDTO.getBno());
   if (optionalBoard.isPresent()) {
       Board board = optionalBoard.get();
       board.changeTitle(boardDTO.getTitle());
       board.changeContent(boardDTO.getContent());
       repository.save(board);
   }
}
```

<aside>
💡 2.6에서는 getOne 메소드가 NoSuchElementException 예외를 던지도록 변경. 따라서 findById 메소드를 사용하여 Optional로 객체를 가져오고, Optional이 존재하는 경우에만 수정 작업을 수행하도록 변경

</aside>

---

### p. 279 boardcontroller 오류

 `boardService` 이 부분에서 오류가 났는데

```jsx
public final BoardService boardService;
```

상단에 이 코드를 추가해주어서 오류가 해결됬다. 

---

### modal.modal is not a function

```jsx
modal.show(); 로 변경 hide도 마찬가지
```

---

### p.405 @request.param

```jsx
getFile(String fileName)을 
getFile(@RequestParam("fileName") String fileName) 변경
```

```jsx
uploadFile(MultipartFile[]을
uploadFile(@RequestParam("uploadFiles") MultipartFile[] 변경
```
