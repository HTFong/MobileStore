package fa.training.restful.controllers;

import fa.training.restful.entities.User;
import fa.training.restful.exceptions.ResourceNotFoundException;
import fa.training.restful.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on : "+ userId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/add")
    public User create(@Validated @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable(value = "id") Long userId, @Validated @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on : "+ userId));
        user.setPassword(userDetails.getPassword());
        final  User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on : "+ userId));
        userRepository.delete(user);
        Map<String, Boolean> responseMap = new HashMap<>();
        responseMap.put("deleted", Boolean.TRUE);
        return responseMap;
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signIn(@Validated @RequestBody User u) {
        User user = userRepository.findByUsernameAndPassword(u.getUsername(), u.getPassword());
        if (user == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(user);
    }

}
