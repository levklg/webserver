package dao;



import crm.model.Client;
import crm.model.Manager;
import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);
    Optional<User> findRandomUser();
    Optional<User> findByLogin(String login);
    public List<Client> getListClient();
    public void addClient(Client client);
    public void addManager(Manager manager);
    public List<Manager> getListMahager();
}