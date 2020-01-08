package repositories;

import dtos.GroupDTO;
import dtos.UserDTO;
import repositories.interfaces.IUserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends Repository<UserDTO> implements IUserRepository {

    @Override
    public List<UserDTO> findByName(String login) {
        List<UserDTO> users = new ArrayList<>();
        if(login == null) return null;
        String query = "SELECT * FROM USER WHERE user_login = '" + login + "';";
        ResultSet result = performQueryWithPreparedStatement(query);

        try {
            while (result.next()) {
                int id = result.getInt("user_id");
                String resultName = result.getString("user_login");
                String description = result.getString("user_password");

                UserDTO user = new UserDTO(id, resultName, description);

                user.setGroups(getGroups(user));
                users.add(user);
            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void add(UserDTO dto) {
        if(dto == null) return;
        String query = "INSERT INTO User (user_login, user_password) VALUES (\'" + dto.getLogin() + "\', \'" + dto.getPassword() + "\');";
        performQueryWithStatement(query);
    }

    @Override
    public void update(UserDTO dto) {
        if (dto == null) return;
        String query = "UPDATE User SET user_login = '" + dto.getLogin() + "', user_password = '" + dto.getPassword() + "' WHERE user_id = " + dto.getId() + ";";
        performQueryWithStatement(query);
    }

    @Override
    public void addOrUpdate(UserDTO dto) {
        if(dto == null) return;
        String query = "INSERT INTO User (user_login, user_password) " +
                "VALUES ('" + dto.getLogin() + "', '" + dto.getPassword() + "') " +
                "ON DUPLICATE KEY UPDATE user_login = '" + dto.getLogin() + "', user_password = '" + dto.getPassword() + "';" ;
        performQueryWithStatement(query);
    }


    @Override
    public void delete(UserDTO dto) {
        if (dto == null) return;
        String query = "DELETE FROM User WHERE user_id = " + dto.getId() + ";";
        performQueryWithStatement(query);
    }

    @Override
    public UserDTO findById(int id) {
        if(id <= 0) return null;
        String query = "SELECT * FROM User WHERE user_id = " + id + ";";
        ResultSet result = performQueryWithPreparedStatement(query);

        try {
            while (result.next()) {
                String login = result.getString("user_login");
                String password = result.getString("user_password");
                result.close();
                return new UserDTO(id, login, password);
            }


        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getCount() {
        int count = 0;

        String query = "SELECT * FROM User;";
        ResultSet result = performQueryWithPreparedStatement(query);

        try {
            while (result.next()) {
                count++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public boolean exists(UserDTO dto) {
        if (dto == null) return false;

        UserDTO user = findById(dto.getId());
        if (user == null) return false;

        if(user.getLogin().equals(dto.getLogin()) && user.getPassword().equals(dto.getPassword())){
            if (getGroups(user) == null && getGroups(dto) == null){
                return true;
            }else if(getGroups(user) != null && getGroups(dto) != null && getGroups(user).equals(getGroups(dto))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setGroups(UserDTO user, List<GroupDTO> groups) throws SQLException {
        if (_connection == null)
            beginTransaction();

        for (GroupDTO group : groups) {
            assignToGroup(user, group);
        }

        commitTransaction();

    }

    @Override
    public void assignToGroup(UserDTO user, GroupDTO group) throws SQLException {
        if (user == null || group == null) return;
        String query = "INSERT INTO UserGroup (group_id, user_id) VALUES ('" + group.getId() + "', '" + user.getId() + "';";
        performQueryWithStatement(query);
    }

    @Override
    public List<GroupDTO> getGroups(UserDTO user) {
        if (user == null) return null;
        String query = "select AGroup.group_id, AGroup.group_name, AGroup.group_description from AGroup " +
                "inner join UserGroup ON UserGroup.group_id = AGroup.group_id " +
                "inner join User on UserGroup.user_id = User.user_id where User.user_id = " + user.getId();
        ResultSet result = performQueryWithPreparedStatement(query);

        List<GroupDTO> groups = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("group_id");
                String name = result.getString("group_name");
                String description = result.getString("group_description");

                groups.add(new GroupDTO(id, name, description));
            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(groups.isEmpty()){
            return null;
        }
        return groups;
    }

    @Override
    public void removeUserFromGroup(UserDTO user, GroupDTO group) throws SQLException {
        if (user == null || group == null) return;
        String query = "DELETE FROM UserGroup WHERE AGroup.group_id = " + group.getId() + " AND User.user_id = " + user.getId() + ";";
        performQueryWithStatement(query);
    }

}
