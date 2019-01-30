package com.grokonez.jwtauthentication.controller;



import com.grokonez.jwtauthentication.model.Consignment;
import com.grokonez.jwtauthentication.repository.ConsignmentRepository;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api")
public class ConsignmentController {

    @Autowired
    private ConsignmentRepository consignmentRepository;


    //metodo que retorna una remesa de acuerdo a su id

    @GetMapping(path = "consignment/{id}")
    public @ResponseBody
    Optional<Consignment> getConsignment(@PathVariable("id") Integer id){
        long lid = id.longValue();
        return consignmentRepository.findById(lid);
    }

    //method get that extracts consignment from database

    @GetMapping(path = "consignment/all")
    public @ResponseBody Iterable<Consignment> getAllConsignments(){
        return consignmentRepository.findAll();
    }

    //method get that extracts consignment active
    @GetMapping(path = "consignment/allActive")
    public @ResponseBody Iterable<Consignment> getAllConsignmentsActive(){
        return consignmentRepository.findAllByActive();
    }

    //method that return consignment status "inactive"

    @GetMapping(path= "consignment/allInactive")
    public @ResponseBody Iterable<Consignment> getAllConsignmentsInactive(){return consignmentRepository.findAllByInactive();}

    //method that return a new consignment
    @RequestMapping(path = "consignment/new", method = RequestMethod.POST)
    public Integer CreateUser(@RequestBody Consignment consignment) {

        consignmentRepository.save(consignment);
        return 1;


    }

    //metodo que extrae el nombre del archivo exportable al excel


    @GetMapping("/consignment/{consignmentId}/nombre")
    public String getNameExportExcel(@PathVariable (value = "consignmentId") Long consignmentId) {
        String lid = consignmentId.toString();
        String name= consignmentRepository.findNameConsignment(consignmentId) + "NºRemesa" + lid;
        return name;


    }



    //method that modify a consignment
    //with its json {
    //	"status":"algo",
    //	"request":"algo"
    //
    //}

    @PutMapping("/consignment/{id}")
    public Consignment updateStudent(@PathVariable Long id,
                                     @Valid @RequestBody Consignment consignmentUpdated) throws NotFoundException {
        return consignmentRepository.findById(id)
                .map(consignment -> {
                    consignment.setRequest(consignmentUpdated.getRequest());
                    consignment.setStatus(consignmentUpdated.getStatus());
                    consignment.setGovernance(consignmentUpdated.getGovernance());

                    return consignmentRepository.save(consignment);
                }).orElseThrow(() -> new NotFoundException("consignment not found with id " + id));
    }

    //method for update status consignment

    @PutMapping("/consignment/{id}/{status}")
    public Consignment updateStatusConsignment(@PathVariable Long id,
                                               @PathVariable String status,
                                               @Valid @RequestBody Consignment consignmentUpdated) throws NotFoundException {

        return consignmentRepository.findById(id)
                .map(consignment -> {

                    if(consignment.getStatus_bin() == "activo" ){
                        consignment.setStatus_bin(status);

                    }
                    else if(consignment.getStatus_bin()=="inactivo"){
                        consignment.setStatus_bin(status);
                    }
                    else{
                        consignment.setStatus_bin(status);
                    }


                    return consignmentRepository.save(consignment);
                }).orElseThrow(() -> new NotFoundException("Consignment not found!"));

    }
    //metodo para actualizar el estado de la remesa, para ser enviada a la upf para su revision

    @PutMapping("/consignmentSendUPF/{id}/{status}")
    public Consignment updateStatusConsignmentForUPF(@PathVariable Long id,
                                               @PathVariable String status,
                                               @Valid @RequestBody Consignment consignmentUpdated) throws NotFoundException {

        return consignmentRepository.findById(id)
                .map(consignment -> {

                    if(consignment.getStatus()=="Realizando" ){
                        consignment.setStatus(status);
                        return consignmentRepository.save(consignment);

                    }
                    if(consignment.getStatus()=="Enviada UPF"){
                        consignment.setStatus(status);
                        return consignmentRepository.save(consignment);
                    }
                    if(consignment.getStatus()!="Realizando" && consignment.getStatus()!="Enviada UPF" ) {
                        consignment.setStatus(status);
                        return consignmentRepository.save(consignment);
                        //consignment.setStatus(status);
                    }

                    return consignmentRepository.save(consignment);
                }).orElseThrow(() -> new NotFoundException("Consignment not found!"));

    }
    @Autowired
    private JavaMailSender sender;

    //metodo para aprobar, aprobar parcialmente o rechazar la remesa

    @PutMapping("/consignmentDecision/{id}/{status}")
    public Consignment updateStatusConsignmentForDecision(@PathVariable Long id,
                                                     @PathVariable String status,
                                                     @Valid @RequestBody Consignment consignmentUpdated) throws NotFoundException {

        String correo= consignmentRepository.findEmailOfCreateConsignment(id);
        String correo_prueba = "f.lagos18615@gmail.com";


        return consignmentRepository.findById(id)
                .map(consignment -> {
                    //if(consignment.getStatus()=="Realizando"){
                      //  consignment.setStatus(consignment.getStatus());
                    //}
                    sendMail(correo_prueba, id, status);


                    if(consignment.getStatus()=="Enviada UPF"){
                        consignment.setStatus(status);

                    }
                    else {
                        consignment.setStatus(status);
                    }

                   // else{
                     //   if(consignment.getStatus()=="Aprobado" || consignment.getStatus()=="Aprobado Parcialmente" || consignment.getStatus()=="Rechazado" ){
                       //     consignment.setStatus(consignment.getStatus());
                            //consignment.setStatus(status);
                        //}

                    //}



                    return consignmentRepository.save(consignment);
                }).orElseThrow(() -> new NotFoundException("Consignment not found!"));

    }

    //method that delete consignment for id

    @DeleteMapping("/consignment/{id}")
    public Integer deleteConsignments(@PathVariable long id) {

        consignmentRepository.deleteById(id);
        return 1;
    }

    //metodo que se encarga de enviar correo
    public void sendMail(String correo, Long num_remesa, String estado) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(correo);
            String duda = "Envie un correo a mdurand@interior.gov.cl o tduran@interior.gov.cl";
            helper.setText("La remesa nº" + num_remesa + " ha sido respondida, su estado es: " + estado + ", cualquier duda consulte la plataforma web o \n " + duda);

            helper.setSubject("La remesa nº "+ num_remesa + ", ha sido respondida"  );
        } catch (MessagingException e) {
            e.printStackTrace();
            //return "Error while sending mail ..";
        }
        sender.send(message);
        //return "Mail Sent Success!";
    }
}
