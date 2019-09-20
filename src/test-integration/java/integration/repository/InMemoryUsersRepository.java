package integration.repository;

import org.openchat.users.repository.UsersRepository;
import org.openchat.users.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUsersRepository implements UsersRepository {

    private List<User> users = new ArrayList<>();

    @Override
    public void save(User user) {
        users.add(user);
    }

    @Override
    public Optional<User> findByName(String username) {
        return users.stream()
                .filter(user -> user.getName().equals(username))
                .findAny();
    }
}
