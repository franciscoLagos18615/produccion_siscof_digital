package com.grokonez.jwtauthentication.controller;



import com.grokonez.jwtauthentication.model.Budget;
import com.grokonez.jwtauthentication.model.Consignment;
import com.grokonez.jwtauthentication.repository.BudgetRepository;
import com.grokonez.jwtauthentication.repository.ConsignmentRepository;
import com.grokonez.jwtauthentication.repository.ItemRepository;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.math.BigDecimal;
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

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JavaMailSender sender;


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
    /*
    @RequestMapping(path = "consignment/new", method = RequestMethod.POST)
    public Integer CreateUser(@RequestBody Consignment consignment) {

        consignmentRepository.save(consignment);
        return 1;


    }*/
    //metodo para crear una remesa
    @PostMapping(value = "/consignment/{budgetId}/new")

    public Consignment createConsignment(@PathVariable Long budgetId,

                               @Valid @RequestBody Consignment consignment) throws NotFoundException {
        return budgetRepository.findById(budgetId)
                .map(budget -> {

                    consignment.setBudget(budget);

                    return consignmentRepository.save(consignment);
                }).orElseThrow(()-> new NotFoundException("Presupuesto no existe!"));

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
        String correo_send = consignmentRepository.findEmailOfCreateConsignment(id);
        String me = "f.lagos18615@gmail.com";
        return consignmentRepository.findById(id)
                .map(consignment -> {

                    if((consignment.getStatus()).compareTo("Realizando")==0){
                        if(consignment.getStatus()=="Realizando" ){
                            consignment.setStatus(status);
                            sendMailFinanzas(correo_send, consignment.getId_consignment());
                            //sendMailFinanzas(me, consignment.getId_consignment());
                            return consignmentRepository.save(consignment);

                        }
                        if(consignment.getStatus()=="Enviada UPF"){
                            consignment.setStatus(status);
                            sendMailFinanzas(correo_send, consignment.getId_consignment());
                            //sendMailFinanzas(me, consignment.getId_consignment());
                            return consignmentRepository.save(consignment);
                        }
                        if(consignment.getStatus()!="Realizando" && consignment.getStatus()!="Enviada UPF" ) {
                            consignment.setStatus(status);
                            sendMailFinanzas(correo_send, consignment.getId_consignment());
                            //sendMailFinanzas(me, consignment.getId_consignment());
                            return consignmentRepository.save(consignment);
                            //consignment.setStatus(status);
                        }
                    }
                    else{
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
                    }



                    return consignmentRepository.save(consignment);
                }).orElseThrow(() -> new NotFoundException("Consignment not found!"));

    }


    //metodo para aprobar, aprobar parcialmente o rechazar la remesa

    @PutMapping("/consignmentDecision/{id}/{statusDecision}")
    public Consignment updateStatusConsignmentForDecision(@PathVariable Long id,
                                                        @PathVariable String statusDecision,
                                                      @Valid @RequestBody Consignment consignmentUpdated) throws NotFoundException {

        /**descomentar esta linea, una vez que comienzen a crear los usuarios reales del sistema*/
        //String correo= consignmentRepository.findEmailOfCreateConsignment(id);
        String correo_prueba = "f.lagos18615@gmail.com";
        String correo_user = consignmentRepository.findEmailOfCreateConsignment(id);
        String correo_user_optional = consignmentRepository.findEmailOptional(correo_user);
        Budget buUpdate = budgetRepository.findById2(consignmentUpdated.getBudget().getBudget_id());
        BigDecimal sumTotalRemesa = itemRepository.findTotalConsignmentByInBigDecimal(consignmentUpdated.getId_consignment());


        return consignmentRepository.findById(id)
                .map(consignment -> {
                    //if(consignment.getStatus()=="Realizando"){
                      //  consignment.setStatus(consignment.getStatus());
                    //}
                    if(correo_user!=null){
                        sendMail(correo_user, id, statusDecision);
                    }

                    if(correo_user_optional!=null){
                        sendMail(correo_user_optional,id,statusDecision);
                    }
                    String estado = consignmentUpdated.getStatus();
                    String estado2= consignment.getStatus();

                    if(estado.compareTo(estado2) == 0){
                        consignment.setStatus(statusDecision);
                        //condicion para descontar de la tabla presupuesto en el campo presupuesto el valor total de la remesa
                        if( sumTotalRemesa.compareTo(buUpdate.getBudget()) ==-1 || sumTotalRemesa.compareTo(buUpdate.getBudget()) ==0 ){
                            BigDecimal newBudget= buUpdate.getBudget().subtract(sumTotalRemesa);
                            buUpdate.setBudget(newBudget);
                            budgetRepository.save(buUpdate);
                            return consignmentRepository.save(consignment);
                        }

                        //

                    }


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

    //metodo para enviar a la unidad de finanzas de la upf que tiene una remesa por revisar
    public void sendMailFinanzas(String correo, Long num_remesa) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(correo);
            helper.setText("La remesa nº" + num_remesa + " ha sido enviada para poder revisar, cualquier duda consulte la plataforma web");

            helper.setSubject("La remesa nº "+ num_remesa + ", ha sido enviada para revisar " );
        } catch (MessagingException e) {
            e.printStackTrace();

        }
        sender.send(message);

    }

}
