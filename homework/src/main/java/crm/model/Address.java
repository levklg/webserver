package crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address")
    private String street;


    public Address(Long id, String street) {
        if(id != null)  this.id = id;
        this.street = street;

    }

    public Address(String street) {
         this.street = street;

    }

    public String getStreet() {
        return street;
    }

    public Address(){

    }
}
