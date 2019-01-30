package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.message.response.ResponseMessage;
import com.grokonez.jwtauthentication.model.Remesa;
import com.grokonez.jwtauthentication.repository.RemesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/remesa")
public class RemesaController {

    @Autowired
    private RemesaRepository remesaRepository;

    @RequestMapping(path = "new", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> CreateUser(@RequestBody Remesa remesa) {

        remesaRepository.save(remesa);
        return new ResponseEntity<>(new ResponseMessage("Remesa Creada Correctamente  !"), HttpStatus.OK);


    }
}
