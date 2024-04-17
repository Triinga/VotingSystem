package com.tringa.votingSystem.Repositories;

import com.tringa.votingSystem.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate, Integer> {
    public Candidate findById(Long id);
}
