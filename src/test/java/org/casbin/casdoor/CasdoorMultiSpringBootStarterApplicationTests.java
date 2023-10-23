package org.casbin.casdoor;

import org.casbin.casdoor.client.*;
import org.casbin.casdoor.config.CasdoorAutoConfigure;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = {CasdoorAutoConfigure.class})
class CasdoorMultiSpringBootStarterApplicationTests {

    @Autowired
    private ClientManager clientManager;

    @Test
    void testUserService() throws IOException {
        UserService service = clientManager.getService("built-in", UserService.class);
        List<User> users = service.getUsers();
        System.out.println(users);
    }
}
