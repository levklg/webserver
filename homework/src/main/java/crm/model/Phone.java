package crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Id;
import javax.persistence.*;



import static javax.persistence.GenerationType.SEQUENCE;


@Entity
@Data
@AllArgsConstructor
@Table(name = "phones")
public class Phone {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;


    @JoinColumn(name = "number")
    private String number = "";

    @ManyToOne
    @JoinColumn(name="client_id", insertable=false, updatable=false, nullable=false)
    private Client client;

    public Phone(String number){
       this.number = (number);

    }

    public Phone(){

    }

    public String getNumber(){
        String s = this.number;
        return s ;
    }



}
