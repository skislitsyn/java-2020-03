package ru.otus.core.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import skislitsyn.core.model.Address;
import skislitsyn.core.model.Phone;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "UserGenerator")
    @TableGenerator(name = "UserGenerator", initialValue = 127, table = "hibernate_sequence_fast_forward")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(targetEntity = Phone.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private List<Phone> phones;

    public User() {
    }

    public User(long id, String name) {
	this.id = id;
	this.name = name;
    }

    public User(long id, String name, Address address, List<Phone> phones) {
	this.id = id;
	this.name = name;
	this.address = address;
	this.phones = phones;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return "User{" + "id=" + id + ", name='" + name + '\'' + ", address=" + address + ", phones=" + phones + '}';
    }
}
