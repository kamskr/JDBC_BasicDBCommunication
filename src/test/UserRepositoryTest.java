package test;

import dtos.UserDTO;
import org.junit.Assert;
import org.junit.Test;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;

import java.util.List;

public final class UserRepositoryTest extends RepositoryTestBase<UserDTO, IUserRepository> {

    private UserDTO user1 = new UserDTO(8,"kamil", "1234");
    private UserDTO userUpdate = new UserDTO(7,"krzysztof", "1111");
    private UserRepository repository = new UserRepository();

    @Test
    public void add() {
//        repository.add(user1);

        Assert.assertTrue(repository.exists(user1));
    }

    @Test
    public void update() {
        repository.update(userUpdate);
        Assert.assertTrue(repository.exists(userUpdate));
    }

    @Test
    public void addOrUpdate() {
        repository.addOrUpdate(user1);
    }

    @Test
    public void delete() {
        repository.delete(user1);
        Assert.assertTrue(!repository.exists(userUpdate));
    }

    @Test
    public void findById() {
        UserDTO test = repository.findById(8);


        Assert.assertTrue(test.equals(user1));
    }

    @Test
    public void findByName() {
        List<UserDTO> test = repository.findByName("kamil");
        Assert.assertTrue(test.size() > 0);
    }

    @Override
    protected IUserRepository Create() {
        return repository;
    }
}