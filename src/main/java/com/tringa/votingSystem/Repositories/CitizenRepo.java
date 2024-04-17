package com.tringa.votingSystem.Repositories;

import com.tringa.votingSystem.Entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepo extends JpaRepository<Citizen, Integer> {
//    public Citizen findByEmailOrName(String email, String name);
    public Citizen findByEmail(String email);


}
