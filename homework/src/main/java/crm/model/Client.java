package crm.model;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "clients")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name ;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<Phone> phoneList;

    @Transient
    private List<String> listNumber;

    public Client(String name) {
        this.id = null;
        this.name = name;


    }

    public Client(Long id, String name,String number) {
        this.id = id;
        this.name = name;
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone(number);
        phones.add(phone);
        this.phoneList = phones;

    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client() {
    }

    public Client( String name,  String number ) {
        this.id = null;
        this.name = name;
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone(number);
        phones.add(phone);
        this.phoneList = phones;
    }

    public Client(Long id, String name, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.phoneList = phones;

    }




    @Override
    public Client clone() {
        return  new Client(this.id, this.name, this.phoneList);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListNumber(){

         listNumber = new ArrayList<>();
        for (Phone phone : this.phoneList) {
            listNumber.add(phone.getNumber());
        }
        return listNumber;
    }


}