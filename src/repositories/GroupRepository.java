package repositories;

import dtos.GroupDTO;
import dtos.UserDTO;
import repositories.interfaces.IGroupRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository extends Repository<GroupDTO> implements IGroupRepository {

    @Override
    public void add(GroupDTO dto) {
        if(dto == null) return;
        String query = "INSERT INTO AGROUP (group_name, group_description) VALUES ('" + dto.getName() + "', '" + dto.getDescription() + "');";
        performQueryWithStatement(query);
    }

    @Override
    public void update(GroupDTO dto) {
        if (dto == null) return;
        String query = "UPDATE AGroup SET group_name = '" + dto.getName() + "', group_description = '" + dto.getDescription() + "' WHERE group_id = " + dto.getId() + ";";
        performQueryWithStatement(query);
    }

    @Override
    public void addOrUpdate(GroupDTO dto) {
        if(dto == null) return;
        String query = "INSERT INTO AGroup (group_name, group_description) " +
                "VALUES ('" + dto.getName() + "', '" + dto.getDescription() + "') " +
                "ON DUPLICATE KEY UPDATE group_name = '" + dto.getName() + "', group_description = '" + dto.getDescription() + "';" ;
        performQueryWithStatement(query);
    }

    @Override
    public void delete(GroupDTO dto) {
        if (dto == null) return;
        String query = "DELETE FROM AGroup WHERE group_id = " + dto.getId() + ";";
        performQueryWithStatement(query);
    }

    @Override
    public int getCount() {
        int count = 0;

        String query = "SELECT * FROM AGroup;";
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
    public boolean exists(GroupDTO dto) {
        if (dto == null) return false;

        GroupDTO group = findById(dto.getId());

        if(group == null) return false;

        if(group.getName().equals(dto.getName()) && group.getDescription().equals(dto.getDescription())){
            if (getUsers(group) == null && getUsers(dto) == null){
                return true;
            }else if(getUsers(group) != null && getUsers(dto) != null && getUsers(group).equals(getUsers(dto))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<UserDTO> getUsers(GroupDTO group) {
        if (group == null) return null;
        String query = "SELECT * FROM User " +
                "INNER JOIN UserGroup ON User.user_id = UserGroup.user_id " +
                "INNER JOIN AGroup ON UserGroup.group_id = AGroup.group_id WHERE AGroup.group_id = " + group.getId() + ";";

        ResultSet result = performQueryWithPreparedStatement(query);

        List<UserDTO> users = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("user_id");
                String login = result.getString("user_login");
                String password = result.getString("user_password");

                users.add(new UserDTO(id, login, password));
            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }



        return users;
    }

    @Override
    public List<GroupDTO> findByName(String name) {
        List<GroupDTO> groups = new ArrayList<>();
        if(name == null) return null;
        String query = "SELECT * FROM AGROUP WHERE group_name = '" + name + "';";
        ResultSet result = performQueryWithPreparedStatement(query);

        try {
            while (result.next()) {
                int id = result.getInt("group_id");
                String resultName = result.getString("group_name");
                String description = result.getString("group_description");

                GroupDTO group = new GroupDTO(id, resultName, description);
                System.out.println(group);
                group.setUsers(getUsers(group));
                groups.add(group);
            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public GroupDTO findById(int id) {
        if(id <= 0) return null;
        String query = "SELECT * FROM AGROUP WHERE group_id = " + id + ";";
        ResultSet result = performQueryWithPreparedStatement(query);

        try {
            while (result.next()) {
                String resultName = result.getString("group_name");
                String description = result.getString("group_description");

                return new GroupDTO(id, resultName, description);

            }
            result.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }




}
