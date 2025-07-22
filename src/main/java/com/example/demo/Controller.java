package com.example.demo;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:8081", exposedHeaders = "Authorization")
@RestController
public class Controller {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil JwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> responseJwtToken(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            ClientDetails user = (ClientDetails) authentication.getPrincipal();
            String jwtToken = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                    .build();

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> addNewUser(@RequestBody AddUserRequest addUserRequest,
            UriComponentsBuilder ucb) {
        if(addUserRequest.getPassword().isEmpty() || addUserRequest.getPassword().isEmpty() || addUserRequest.getEmail().isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("message", "Fields cannot be empty!"));
        }
        if (userRepository.existsByUsername(addUserRequest.getUsername())) {
            Map<String, String> error = Map.of("field", "username", "message", "Username already taken!");
            return ResponseEntity.badRequest().body(error);
        } else if (userRepository.existsByEmail(addUserRequest.getEmail())) {
            Map<String, String> error = Map.of("field", "email", "message",
                    "User already registered with the same email!");
            return ResponseEntity.badRequest().body(error);
        } else {
            User newUser = new User(null, addUserRequest.getUsername(),
                    passwordEncoder.encode(addUserRequest.getPassword()), addUserRequest.getEmail(), List.of("USER"));
            userRepository.save(newUser);
            URI location = ucb.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();

            return ResponseEntity.created(location).body(Map.of("message", "User Successfully created!"));
        }
    }

    @PostMapping("/update/{newUsername}")
    public ResponseEntity<Map<String, String>> updateUsername(@PathVariable String newUsername, Principal principal){
        String username = principal.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(!optionalUser.isEmpty()) {
            User user = optionalUser.get();
            user.setUsername(newUsername);
            userRepository.save(user);
            return ResponseEntity.ok().body(Map.of("message", "Username updated Successfully"));
        }   

        else {
            return ResponseEntity.notFound().build();
        }
    } 
}
