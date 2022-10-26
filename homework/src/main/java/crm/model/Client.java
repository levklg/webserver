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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;








    public Client() {
    }

    public Client( String name,  String number ) {
        this.id = null;
        this.name = name;
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone(number);
        phones.add(phone);
        this.address = new Address("");
        this.phoneList = phones;
    }

    public Client(Long id, String name, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.phoneList = phones;
        this.address = new Address("");

    }

    public Client(Long id, String name, List<Phone> phones, Address address) {
        this.id = id;
        this.name = name;
        this.phoneList = phones;
        this.address = address;

    }
    public Client( String name,  String number, String address ) {
        this.id = null;
        this.name = name;
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone(number);
        phones.add(phone);
        this.address = new Address(address);
        this.phoneList = phones;
    }



    @Override
    public Client clone() {
        return  new Client(this.id, this.name, this.phoneList, this.address);
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

        List listNumber = new ArrayList<>();
        for (Phone phone : this.phoneList) {
            listNumber.add(phone.getNumber());
        }
        return listNumber;
    }


    public String getAddress() {
        return address.getStreet();
    }
}