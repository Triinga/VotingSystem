package com.tringa.votingSystem.Controller;

import com.tringa.votingSystem.Entity.Candidate;
import com.tringa.votingSystem.Entity.Citizen;
import com.tringa.votingSystem.Repositories.CandidateRepo;
import com.tringa.votingSystem.Repositories.CitizenRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.jboss.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


import java.util.List;

@Controller
public class VotingController {

    public final Logger logger = Logger.getLogger(VotingController.class);
    @Autowired
    CitizenRepo citizenRepo;

    @Autowired
    CandidateRepo candidateRepo;

    @RequestMapping("/")
    public String redirectToLoginOrSignup() {
        // Redirect to the login page or signup page based on your logic
        // For example, you can redirect to the login page:
        return "redirect:/login";
    }
    @RequestMapping("/login")
    public String loginForm() {
        return "login.html";
    }
    @RequestMapping("/signup")
    public String signupForm() {
        return "signup.html";
    }

    @RequestMapping("/dosignup")
    public String doSignup(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        Citizen existingCitizen = citizenRepo.findByEmail(email);
        if (existingCitizen != null) {
            // Email already exists, handle accordingly (e.g., display error message)
            // For simplicity, let's redirect back to the signup page with an error message
            return "redirect:/signup?error=emailExists";
        }
        // Hash the password
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            // Handle error in password hashing
            return "redirect:/signup?error=hashingError";
        }

        Citizen citizen = new Citizen();
        citizen.setName(name);
        citizen.setEmail(email);
        citizen.setPassword(hashedPassword);
        // Set has_voted to false (0) for new citizens
        citizen.setHasVoted(false);
        citizenRepo.save(citizen);
        return "redirect:/login";
    }

    @RequestMapping("/dologin")
    public String doLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session){
        logger.info("getting citizen from database");
        Citizen citizen = citizenRepo.findByEmail(email);
        if (citizen != null) { // Check if citizen exists
            // Hash the input password for comparison
            String hashedPassword = hashPassword(password);
            if (hashedPassword != null && hashedPassword.equals(citizen.getPassword()) && !citizen.getHasVoted()) {
                session.setAttribute("citizen", citizen);
                List<Candidate> candidates = candidateRepo.findAll();
                model.addAttribute("candidates", candidates);
                return "/performVoted.html";
            } else {
                return "/alreadyVoted.html";
            }
        } else {
            // Handle case when citizen does not exist (e.g., display error message or redirect to signup)
            return "redirect:/signup";
        }
    }


    @RequestMapping("/voteFor")
    public String voteFor(@RequestParam Long id, HttpSession session){
        Citizen citizen = (Citizen)session.getAttribute("citizen");
        if (!citizen.getHasVoted()){
            citizen.setHasVoted(true);
            Candidate c = candidateRepo.findById((long)id);
            logger.info("voting for candidate " + c.getName());
            c.setNumberOfVotes(c.getNumberOfVotes()+1);
            candidateRepo.save(c);
            citizenRepo.save(citizen);
            return "voted.html";
        }
        return "alreadyVoted.html";
    }

    // Method to hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] hashedBytes = md.digest();
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
