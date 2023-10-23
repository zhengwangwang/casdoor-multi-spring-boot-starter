[简体中文](README_cn.md) | English

# Casdoor Multi Tenant SpringBoot Starter

Casdoor Multi Tenant SpringBoot Starter is designed to help you easily integrate [Casdoor](https://github.com/casbin/casdoor) into
your Multi Tenant Spring Boot project.

Please use [casdoor-spring-boot-starter](https://github.com/casdoor/casdoor-spring-boot-starter) for single tenant projects

# What you need

The Casdoor should be deployed.

You can refer to the Casdoor official documentation for the [Server Installation](https://casdoor.org/docs/basic/server-installation/).

After a successful deployment, you need to ensure:
- The Casdoor server is successfully running on `http://localhost:8000`.
- Open your favorite browser and visit `http://localhost:8000`, you will see the login page of Casdoor.
- Input `admin` and `123` to test login functionality is working fine.

# Quickstart

Add ```casdoor-multi-spring-boot-starter``` to the Spring Boot project.

For Apache Maven:

```Maven
<!-- https://mvnrepository.com/artifact/org.casbin/casdoor-spring-boot-starter -->
<dependency>
    <groupId>org.casbin</groupId>
    <artifactId>casdoor-multi-spring-boot-starter</artifactId>
    <version>1.x.y</version>
</dependency>
```

For Gradle:

```gradle
// https://mvnrepository.com/artifact/org.casbin/casdoor-spring-boot-starter
implementation group: 'org.casbin', name: 'casdoor-multi-spring-boot-starter', version: '1.x.y'
```

# Configure your properties

Initialize the client, each client requires 6 parameters, all of which are string types.

| Name (in order)  | Must | Description                                         |
|------------------|------|-----------------------------------------------------|
| endpoint         | Yes  | Casdoor Server Url, such as `http://localhost:8000` |
| clientId         | Yes  | Application.client_id                               |
| clientSecret     | Yes  | Application.client_secret                           |
| certificate      | Yes  | The public key for the Casdoor application's cert   |
| organizationName | Yes  | Application.organization                            |
| applicationName  | No   | Application.name                                    |


You can use the default config provider, which uses the springboot configuration file. It should be noted that each client's organizationName is unique.

You can use Java properties or YAML files to init as below.

For properties:

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

For yaml:

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
    # client2: as more
```


# Custom Config Provider

If the client config is dynamic, such as being stored in a database, then the configuration can be provided to the ClientManager through a custom configuration provider

Implement `org.casbin.casdoor.client.ConfigProvider` interface and inject it into the spring container, ClientManager will automatically use a custom configuration provider. Examples are as follows:

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

# Get the ClientManager and use

Now provide ClientManager. You can create them as below in SpringBoot project.

```java
@Resource
private ClientManager clientManager;
```

Get the Service and use from ClientManager as below：

```java
UserService service = clientManager.getService("built-in", UserService.class);
List<User> users = service.getUsers();
```
all service in package `org.casbin.casdoor.service`.

provide 19 services：`AccountService`, `ApplicationService`, `AuthService`, `CertService`, `EmailService`, 
`EnforcerService`, `GroupService`, `ModelService`, `OrganizationService`, `PaymentService`, `PermissionService`, 
`ProviderService`, `RecordService`, `ResourceService`, `RoleService`, `SessionService`, `SmsService`, 
`TokenService`, `UserService`

Examples of APIs are shown below:

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

# What's more

You can explore the following projects/docs to learn more about the integration of Java with Casdoor.

- [casdoor-java-sdk](https://github.com/casdoor/casdoor-java-sdk)
- [casdoor-spring-boot-example](https://github.com/casdoor/casdoor-spring-boot-example)
- [casdoor-spring-boot-security-example](https://casdoor.org/docs/category/spring-security)
- [casdoor-spring-boot-shiro-example](https://github.com/casdoor/casdoor-spring-boot-shiro-example)