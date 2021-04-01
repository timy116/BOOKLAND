#BOOKLAND

---

此專案為一個線上藝術書店

##Development

---

將 `application.yml-sample` 改成 `application.yml` 並將裡面的參數替換成自己的。

e.g.
```yaml
spring:
  ...
  
  datasource:
    driver-class-name: {YOUR_DRIVER}
    url: jdbc:{YOUR_DATABASE}://{YOUR_HOST}:3306/{DB_NAME}?serverTimezone=UTC&useSSL=false
    username: {YOUR_USERNAME}
    password: {YOUR_PASSWORD}
```

<br>

替換成:
```yaml
spring:
  ...
  
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.31.237:3306/test?serverTimezone=UTC&useSSL=false
    username: root
    password: root
```