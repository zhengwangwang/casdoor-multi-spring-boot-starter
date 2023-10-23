简体中文 | [English](README.md)

# Casdoor多租户SpringBoot项目启动器

该启动器旨在帮助您轻松集成[Casdoor](https://github.com/casbin/casdoor) 到您的多租户Spring Boot项目.

单租户项目请使用[casdoor-spring-boot-starter](https://github.com/casdoor/casdoor-spring-boot-starter).

# 依赖环境

部署Casdoor

您可以参考Casdoor [服务安装](https://casdoor.org/zh/docs/basic/server-installation )官方文档了解

成功部署后，您需要确保：
- Casdoor服务器已成功运行在`http://localhost:8000`。
- 打开您喜爱的浏览器并访问`http://localhost:8000`，您将看到Casdoor的登录页面。
- 输入“admin”和“123”以测试登录功能运行良好。

# 快速开始

添加 ```casdoor-multi-spring-boot-starter``` 到SpringBoot项目依赖.

Apache Maven示例:

```Maven
<dependency>
    <groupId>org.casbin</groupId>
    <artifactId>casdoor-multi-spring-boot-starter</artifactId>
    <version>1.x.y</version>
</dependency>
```

Gradle示例:

```gradle
implementation group: 'org.casbin', name: 'casdoor-multi-spring-boot-starter', version: '1.x.y'
```

# 配置客户端配置提供器

每个客户端需要6个配置参数，他们都是字符串类型。

| 参数名          | 是否必须 |    描述                                             |
|------------------|------|-----------------------------------------------------|
| endpoint         | 是   | Casdoor服务url, 例如 `http://localhost:8000`          |
| clientId         | 是   | 应用程序.client_id                               |
| clientSecret     | 是   | 应用程序.client_secret                           |
| certificate      | 是   | Casdoor应用程序证书的公钥                              |
| organizationName | 是   | Casdoor应用程序的所属组织                              |
| applicationName  | 否   | 应用程序名字                                          |

你可以使用默认的配置提供器，默认的配置提供器使用springboot配置文件，需要注意的是每个客户端的organizationName唯一。

您可以使用properties或YAML文件进行初始化，如下所示：

properties示例:

```properties
casdoor.client.client1.endpoint = http://localhost:8000
casdoor.client.client1.clientId = <client-id>
casdoor.client.client1.clientSecret = <client-secret>
casdoor.client.client1.certificate = <jwt-public-key>
casdoor.client.client1.organizationName = built-in
casdoor.client.client1.applicationName = app-built-in

# casdoor.client.client2.endpoint = http://localhost:8000
# casdoor.client.client2.clientId = <client-id>
# casdoor.client.client2.clientSecret = <client-secret>
# casdoor.client.client2.certificate = <jwt-public-key>
# casdoor.client.client2.organizationName = built-in
# casdoor.client.client2.applicationName = app-built-in
```

yaml示例:

```yaml
casdoor:
  client:
    client1:
      endpoint: http://localhost:8000
      client-id: <client-id>
      client-secret: <client-secret>
      certificate: <jwt-public-key>
      organization-name: built-in
      application-name: app-built-in
    # client2: 更多客户端配置
```

# 自定义配置提供器

如果客户端配置是动态的，比如配置是存放在数据库中，那么就可以通过自定义配置提供器的方式给ClientManager提供配置

实现`org.casbin.casdoor.client.ConfigProvider`接口，并注入到spring容器中，clientManager会自动使用自定义配置提供器，示例如下：

```java
@Configuration
public class DemoConfig {
    
    @Bean
    public ConfigProvider configProvider(){
        return new DemoConfigProvider();
    }
    
    public static class DemoConfigProvider implements ConfigProvider {
        private final String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" +
                "        MIIE+TCCAuGgAwIBAgIDAeJAMA0GCSqGSIb3DQEBCwUAMDYxHTAbBgNVBAoTFENh\n" +
                "        c2Rvb3IgT3JnYW5pemF0aW9uMRUwEwYDVQQDEwxDYXNkb29yIENlcnQwHhcNMjEx\n" +
                "        MDE1MDgxMTUyWhcNNDExMDE1MDgxMTUyWjA2MR0wGwYDVQQKExRDYXNkb29yIE9y\n" +
                "        Z2FuaXphdGlvbjEVMBMGA1UEAxMMQ2FzZG9vciBDZXJ0MIICIjANBgkqhkiG9w0B\n" +
                "        AQEFAAOCAg8AMIICCgKCAgEAsInpb5E1/ym0f1RfSDSSE8IR7y+lw+RJjI74e5ej\n" +
                "        rq4b8zMYk7HeHCyZr/hmNEwEVXnhXu1P0mBeQ5ypp/QGo8vgEmjAETNmzkI1NjOQ\n" +
                "        CjCYwUrasO/f/MnI1C0j13vx6mV1kHZjSrKsMhYY1vaxTEP3+VB8Hjg3MHFWrb07\n" +
                "        uvFMCJe5W8+0rKErZCKTR8+9VB3janeBz//zQePFVh79bFZate/hLirPK0Go9P1g\n" +
                "        OvwIoC1A3sarHTP4Qm/LQRt0rHqZFybdySpyWAQvhNaDFE7mTstRSBb/wUjNCUBD\n" +
                "        PTSLVjC04WllSf6Nkfx0Z7KvmbPstSj+btvcqsvRAGtvdsB9h62Kptjs1Yn7GAuo\n" +
                "        I3qt/4zoKbiURYxkQJXIvwCQsEftUuk5ew5zuPSlDRLoLByQTLbx0JqLAFNfW3g/\n" +
                "        pzSDjgd/60d6HTmvbZni4SmjdyFhXCDb1Kn7N+xTojnfaNkwep2REV+RMc0fx4Gu\n" +
                "        hRsnLsmkmUDeyIZ9aBL9oj11YEQfM2JZEq+RVtUx+wB4y8K/tD1bcY+IfnG5rBpw\n" +
                "        IDpS262boq4SRSvb3Z7bB0w4ZxvOfJ/1VLoRftjPbLIf0bhfr/AeZMHpIKOXvfz4\n" +
                "        yE+hqzi68wdF0VR9xYc/RbSAf7323OsjYnjjEgInUtRohnRgCpjIk/Mt2Kt84Kb0\n" +
                "        wn8CAwEAAaMQMA4wDAYDVR0TAQH/BAIwADANBgkqhkiG9w0BAQsFAAOCAgEAn2lf\n" +
                "        DKkLX+F1vKRO/5gJ+Plr8P5NKuQkmwH97b8CS2gS1phDyNgIc4/LSdzuf4Awe6ve\n" +
                "        C06lVdWSIis8UPUPdjmT2uMPSNjwLxG3QsrimMURNwFlLTfRem/heJe0Zgur9J1M\n" +
                "        8haawdSdJjH2RgmFoDeE2r8NVRfhbR8KnCO1ddTJKuS1N0/irHz21W4jt4rxzCvl\n" +
                "        2nR42Fybap3O/g2JXMhNNROwZmNjgpsF7XVENCSuFO1jTywLaqjuXCg54IL7XVLG\n" +
                "        omKNNNcc8h1FCeKj/nnbGMhodnFWKDTsJcbNmcOPNHo6ixzqMy/Hqc+mWYv7maAG\n" +
                "        Jtevs3qgMZ8F9Qzr3HpUc6R3ZYYWDY/xxPisuKftOPZgtH979XC4mdf0WPnOBLqL\n" +
                "        2DJ1zaBmjiGJolvb7XNVKcUfDXYw85ZTZQ5b9clI4e+6bmyWqQItlwt+Ati/uFEV\n" +
                "        XzCj70B4lALX6xau1kLEpV9O1GERizYRz5P9NJNA7KoO5AVMp9w0DQTkt+LbXnZE\n" +
                "        HHnWKy8xHQKZF9sR7YBPGLs/Ac6tviv5Ua15OgJ/8dLRZ/veyFfGo2yZsI+hKVU5\n" +
                "        nCCJHBcAyFnm1hdvdwEdH33jDBjNB6ciotJZrf/3VYaIWSalADosHAgMWfXuWP+h\n" +
                "        8XKXmzlxuHbTMQYtZPDgspS5aK+S4Q9wb8RRAYo=\n" +
                "        -----END CERTIFICATE-----";
        Map<String, Config> map = new HashMap<>();
        DemoConfigProvider(){
            map.put("built-in", new Config(
                    "http://localhost:8000",
                    "336ae5b695575ae5c8b3",
                    "a0645fd99657741810af8d48f84d902b7be7c948",
                    CERTIFICATE,
                    "built-in",
                    "app-built-in"
            ));
        }

        @Override
        public Config getConfig(String organizationName) {
            return map.get(organizationName);
        }

        @Override
        public List<Config> getConfigs() {
            return map.values().stream().collect(Collectors.toList());
        }
    }
}
```

# 获取ClientManager并使用

在spring容器中提供ClientManager. 您可以在SpringBoot项目中注入它们，如下所示。

```java
@Resource
private ClientManager clientManager;
```

通过ClientManager获取Service并使用它，如下所示：

```java
UserService service = clientManager.getService("built-in", UserService.class);
List<User> users = service.getUsers();
```
所有Service位于`org.casbin.casdoor.service`包下面。

提供19个Service：`AccountService`, `ApplicationService`, `AuthService`, `CertService`, `EmailService`, 
`EnforcerService`, `GroupService`, `ModelService`, `OrganizationService`, `PaymentService`, `PermissionService`, 
`ProviderService`, `RecordService`, `ResourceService`, `RoleService`, `SessionService`, `SmsService`, 
`TokenService`, `UserService`

API示例如下所示:

- AuthService
  - `String token = authService.getOAuthToken(code, "app-built-in");`
  - `User casdoorUser = authService.parseJwtToken(token);`
- CasdoorUserService
  - `User casdoorUser = userService.getUser("admin");`
  - `User casdoorUser = userService.getUserByEmail("admin@example.com");`
  - `User[] casdoorUsers = userService.getUsers();`
  - `User[] casdoorUsers = userService.getSortedUsers("created_time", 5);`
  - `int count = userService.getUserCount("0");`
  - `CasdoorResponse response = userService.addUser(user);`
  - `CasdoorResponse response = userService.updateUser(user);`
  - `CasdoorResponse response = userService.deleteUser(user);`
- EmailService
  - `CasdoorResponse response = EmailService.sendEmail(title, content, sender, receiver);`
- SmsService
  - `CasdoorResponse response = SmsService.sendSms(randomCode(), receiver);`
- ResourceService
  - `CasdoorResponse response = ResourceService.uploadResource(user, tag, parent, fullFilePath, file);`
  - `CasdoorResponse response = ResourceService.deleteResource(file.getName());`

# 更多信息

您可以探索以下项目/文档，以了解有关Java与Casdoor集成的更多信息。

- [casdoor-java-sdk](https://github.com/casdoor/casdoor-java-sdk)
- [casdoor-spring-boot-example](https://github.com/casdoor/casdoor-spring-boot-example)
- [casdoor-spring-boot-security-example](https://casdoor.org/docs/category/spring-security)
- [casdoor-spring-boot-shiro-example](https://github.com/casdoor/casdoor-spring-boot-shiro-example)