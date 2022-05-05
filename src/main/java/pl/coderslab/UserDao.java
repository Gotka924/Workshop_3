package pl.coderslab;

import pl.coderslab.utils.DbUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    private static final String CREATE_USER = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

    private static final String SELECT_USER_DATA = "SELECT * FROM users where id = ?";

    private static final String UPDATE_USER_DATA = "UPDATE users SET username = ?, email = ?, password = ? where id = ?";

    private static final String DELETE_USER_DATA = "DELETE FROM users WHERE id = ?";

    private static final String SELECT_ALL_USERS = "SELECT * FROM users";

    public static User createUser(User user) {

        try (Connection connect = DbUtil.getConnection()) {
            PreparedStatement statement = connect.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                System.out.println();
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User selectUser(int userId) {
        try (Connection connect = DbUtil.getConnection()) {
            PreparedStatement statement = connect.prepareStatement(SELECT_USER_DATA);

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
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
        return null;
    }

    public void updateUser(User user) {

        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_DATA);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement prepSt = conn.prepareStatement(DELETE_USER_DATA);
            prepSt.setInt(1, userId);
            prepSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            User[] users = new User[0];
            PreparedStatement prepSt = conn.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = prepSt.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId((resultSet.getInt("id")));
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

    private User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }
}
