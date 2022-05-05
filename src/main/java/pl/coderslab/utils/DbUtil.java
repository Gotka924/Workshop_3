package pl.coderslab.utils;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class DbUtil {

    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        return getInstance().getConnection();
    }

    private static DataSource getInstance() {
        if (dataSource == null) {
            try {
                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                dataSource = (DataSource) envContext.lookup("jdbc/users");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }

    public static class UserDao {

        private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?);";
        private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = ?;";
        private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?;";
        private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?;";
        private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users;";


        public void delete(int id) {
            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public String hashPassword(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }

        public User create(User user) {
            try (Connection conn = getConnection()) {
                PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, hashPassword(user.getPassword()));
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));  // WYCIĄGAM INTA z PIERWSZEJ KOLUMNY
                }
                return user;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        public User read(int userId) {
            try (Connection conn = getConnection()) {
                PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {  // "IF" ZAMIAST "WHILE" BO NIE MUSZĘ SIĘ ITEROWAĆ - ALBO JEST NOWA LINIA(TRUE) ALBO NIE(FALSE)
                    // ZWRACA TYLKO 1 UŻYTKOWNIKA. GDYBY ZWRACAŁO WIĘCEJ, TO ZNAK, ŻE "id" NIE JEST UNIKALNE
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    return user;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("NIE MA TAKIEGO UŻYTKOWNIKA W BAZIE DANYCH");
            return null;
        }

        public void update(User user) {
            try (Connection conn = getConnection()) {
                PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, this.hashPassword(user.getPassword()));
                statement.setInt(4, user.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private User[] addToArray(User u, User[] users) {
            User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
            tmpUsers[users.length] = u;
            return tmpUsers;
        }

        public User[] findAll() {
            try (Connection conn = getConnection()) {
                User[] users = new User[0];
                PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    users = addToArray(user, users);
                }
                return users;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
