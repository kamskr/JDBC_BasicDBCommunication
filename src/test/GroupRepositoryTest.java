package test;

import dtos.GroupDTO;
import exception.UnimplementedException;
import org.junit.Assert;
import org.junit.Test;
import repositories.GroupRepository;
import repositories.interfaces.IGroupRepository;

public class GroupRepositoryTest extends RepositoryTestBase<GroupDTO, IGroupRepository> {

    private GroupRepository repository = new GroupRepository();
    GroupDTO group1 = new GroupDTO(1,"Czadowi", "Grupa zrzeszająca ludzi uczących się na pjatk");
    @Test
    public void add() {
//        repository.add(group1);
        Assert.assertTrue(repository.exists(group1));
    }

    @Test
    public void update() {
        repository.update(group1);
        Assert.assertTrue(repository.exists(group1));
    }

    @Test
    public void addOrUpdate() {
        repository.addOrUpdate(group1);
        Assert.assertTrue(repository.exists(group1));
    }

    @Test
    public void delete(){
        repository.delete(group1);
        Assert.assertTrue(!repository.exists(group1));
    }

    @Test
    public void findById() {

        Assert.assertTrue(group1.equals(repository.findById(3)));
    }

    @Test
    public void findByName() {
        Assert.assertTrue(repository.findByName("Czadowi").size() > 0);
    }

    @Override
    protected IGroupRepository Create() {
        return repository;
    }
}