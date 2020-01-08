package repositories.interfaces;

import dtos.GroupDTO;
import dtos.UserDTO;

import java.sql.SQLException;
import java.util.List;


public interface IGroupRepository extends IRepository<GroupDTO> {

    List<GroupDTO> findByName(String name) throws SQLException;

    List<UserDTO> getUsers(GroupDTO group) throws SQLException;

}