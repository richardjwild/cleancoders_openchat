package org.openchat;

import org.openchat.login.api.LoginController;
import org.openchat.users.api.UsersController;
import org.openchat.users.repository.JdbcUsersRepository;
import org.openchat.users.repository.UsersRepository;
import org.openchat.login.service.LoginService;
import org.openchat.users.service.UsersService;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;

public class OpenChatLauncher {

    private static Routes routes;
    private static Properties properties;
    private static UsersController usersController;
    private static UsersService usersService;
    private static UsersRepository usersRepository;
    private static DataSource dataSource;
    private static LoginController loginController;
    private static LoginService loginService;

    public static void main(String[] args) throws IOException {
        wireDependencies();
        new OpenChat(routes).start();
    }

    private static void wireDependencies() throws IOException {
        properties = getEnvironmentProperties();
        dataSource = new PooledDataSourceFactory(properties).getDataSource();
        usersRepository = new JdbcUsersRepository(dataSource);
        usersService = new UsersService(usersRepository);
        loginService = new LoginService(usersService);
        usersController = new UsersController(usersService);
        loginController = new LoginController(loginService);
        routes = new Routes(usersController, loginController);
    }

    private static Properties getEnvironmentProperties() throws IOException {
        String environment = getEnvironment();
        return propertiesFor(environment);
    }

    private static Properties propertiesFor(String environment) throws IOException {
        InputStream propertiesStream = OpenChatLauncher.class.getResourceAsStream(format("/%s-database" +
                ".properties", environment));
        Properties properties = new Properties();
        properties.load(propertiesStream);
        return properties;
    }

    private static String getEnvironment() {
        String environment = System.getenv("ENVIRONMENT");
        if (environment == null)
            throw new IllegalStateException("Environment is not configured");
        else
            return environment;
    }

}
