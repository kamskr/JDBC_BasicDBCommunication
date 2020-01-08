package repositories.interfaces;

import dtos.GroupDTO;
import dtos.UserDTO;
import repositories.interfaces.IRepository;

import java.sql.SQLException;
import java.util.List;

public interface IUserRepository extends IRepository<UserDTO> {

    List<UserDTO> findByName(String nick) throws SQLException;

    void setGroups(UserDTO user, List<GroupDTO> groups) throws SQLException;

    void assignToGroup(UserDTO user, GroupDTO group) throws SQLException;

    List<GroupDTO> getGroups(UserDTO user) throws SQLException;

    void removeUserFromGroup(UserDTO user, GroupDTO group) throws SQLException;

}