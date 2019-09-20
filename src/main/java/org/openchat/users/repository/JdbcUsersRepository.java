package org.openchat.users.repository;

import org.openchat.users.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcUsersRepository implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUsersRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (id,name,password,about) VALUES (?,?,?,?)",
                user.getId().toString(),
                user.getName(),
                user.getPassword(),
                user.getAbout()
        );
    }

    @Override
    public Optional<User> findByName(String username) {
        List<User> users = jdbcTemplate.query(
                "SELECT id,name,password,about FROM users WHERE name=?",
                new Object[] {username},
                (rs, i) -> new User(
                        UUID.fromString(rs.getString(1)),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)));
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}
