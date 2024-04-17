package com.tringa.votingSystem.Entity;

import jakarta.persistence.*;

@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(nullable = false)
    private Boolean hasVoted = false;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
    public Citizen(Long id, String name,String email, String password, Boolean hasVoted) {
        Id = id;
        this.name = name;
        this.hasVoted = hasVoted;
        this.email = email;
        this.password = password;

    }

    public Citizen() {
        super();
    }


}
