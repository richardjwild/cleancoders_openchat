package integration;

import integration.repository.InMemoryUsersRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openchat.OpenChat;
import org.openchat.Routes;
import org.openchat.login.api.LoginController;
import org.openchat.users.api.UsersController;
import org.openchat.login.service.LoginService;
import org.openchat.users.service.UsersService;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        IT_RegistrationAPI.class,
        IT_LoginAPI.class,
        IT_TimelineAPI.class,
        IT_UsersAPI.class,
        IT_FolloweesAPI.class,
        IT_WallAPI.class
})
public class APITestSuit {

    static String BASE_URL = "http://localhost:4321";

    public static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    public static final String DATE_PATTERN = "\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d([+-][0-2]\\d:[0-5]\\d|Z)";

    private static OpenChat openChat;

    @BeforeClass
    public static void setUp() {
        InMemoryUsersRepository usersRepository = new InMemoryUsersRepository();
        UsersService usersService = new UsersService(usersRepository);
        UsersController usersController = new UsersController(usersService);
        LoginService loginService = new LoginService(usersService);
        LoginController loginController = new LoginController(loginService);
        Routes routes = new Routes(usersController, loginController);
        openChat = new OpenChat(routes);
        openChat.start();
        openChat.awaitInitialization();
    }

    @AfterClass
    public static void tearDown() {
        openChat.stop();
    }

}