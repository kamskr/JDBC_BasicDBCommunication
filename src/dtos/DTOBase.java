package dtos;

import java.sql.Connection;
import java.sql.Statement;

public abstract class DTOBase {

    private int _id;


    protected DTOBase() {}

    protected DTOBase(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public boolean hasExistingId() {
        return getId() > 0;
    }

    public int compareTo(DTOBase o) {
        if(o.getId() != getId()) return getId() - o.getId();

        return equals(o) ? 0 : -1;
    }
}
