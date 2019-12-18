package test;

import dtos.GroupDTO;
import exception.UnimplementedException;
import org.junit.Test;
import repositories.IGroupRepository;

public class GroupRepositoryTest extends RepositoryTestBase<GroupDTO, IGroupRepository> {

    @Test
    public void add() {
    }

    @Test
    public void update() {
    }

    @Test
    public void addOrUpdate() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void findByName() {
    }

    @Override
    protected IGroupRepository Create() {
        throw new UnimplementedException();
    }
}