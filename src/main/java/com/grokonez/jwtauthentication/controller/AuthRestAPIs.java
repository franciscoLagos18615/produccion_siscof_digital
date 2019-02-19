package com.grokonez.jwtauthentication.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.grokonez.jwtauthentication.message.request.LoginForm;
import com.grokonez.jwtauthentication.message.request.SignUpForm;
import com.grokonez.jwtauthentication.message.response.JwtResponse;
import com.grokonez.jwtauthentication.message.response.ResponseMessage;
import com.grokonez.jwtauthentication.model.Role;
import com.grokonez.jwtauthentication.model.RoleName;
import com.grokonez.jwtauthentication.model.User;
import com.grokonez.jwtauthentication.repository.RoleRepository;
import com.grokonez.jwtauthentication.repository.UserRepository;
import com.grokonez.jwtauthentication.security.jwt.JwtProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
	}


	@PostMapping("/signup")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail_optional());

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		strRoles.forEach(role -> {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(adminRole);

				break;
            //role de la UPF
			case "upf":
				Role pmRole = roleRepository.findByName(RoleName.ROLE_UPF)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(pmRole);

				break;

                case "gobernacion":
                    Role cmRole = roleRepository.findByName(RoleName.ROLE_GOBERNACION)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(cmRole);

                    break;
                    //role gobernacion->user
			default:
				Role userRole = roleRepository.findByName(RoleName.ROLE_COMPLEJO)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(userRole);
			}
		});

		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}
    //metodo que obtiene un usuario de acuerdo a su id
    @GetMapping(path = "user/{id}")
    public @ResponseBody
    Optional<User> getUser(@PathVariable("id") Integer id){
        long lid = id.longValue();
        return userRepository.findById(lid);
    }

    //metodo que extrae el usuario de acuerdo a su username
    @GetMapping(path = "username/{username}")
    public @ResponseBody
    Optional<User> getUserForUsername(@PathVariable("username") String username){
        //long lid = id.longValue();
        return userRepository.findByUsername(username);
    }

    //method get that extracts budgets from database

    @GetMapping(path = "/user/all")
    public @ResponseBody Iterable<User> getAllUser(){
        return userRepository.findAll();
    }

	//method for update user
    @PutMapping("/UserUpdate/{id}")
    public User updateUser(@PathVariable Long id,
                               @Valid @RequestBody User userUpdated) throws NotFoundException {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(user.getName());
                    user.setUsername(user.getUsername());
                    //user.setPassword(userUpdated.getPassword());
                    //user.setUsername(userUpdated.getUsername());
                    //user.setPassword(userUpdated.encoder.encode(getPassword()));
                    user.setPassword(encoder.encode(userUpdated.getPassword()));
                    user.setRoles(userUpdated.getRoles());
                    user.setEmail(userUpdated.getEmail());

                    return userRepository.save(user);
                    //return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);

                }).orElseThrow(() -> new NotFoundException("user not found with id " + id));
    }
}