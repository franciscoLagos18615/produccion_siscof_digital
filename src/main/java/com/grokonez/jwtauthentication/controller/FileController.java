package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.File;
import com.grokonez.jwtauthentication.model.Item;
import com.grokonez.jwtauthentication.model.View;
import com.grokonez.jwtauthentication.repository.FileRepository;
import com.grokonez.jwtauthentication.repository.ItemRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.annotation.JsonView;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api")
public class FileController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private FileRepository fileRepository;


    //method post that create un file asociate a items
    @PostMapping(value="/file/{itemId}/upload",produces = MediaType.APPLICATION_JSON_VALUE,  consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String uploadMultipartFile(@RequestParam("file") MultipartFile file,
                                      @PathVariable Long itemId
                                      ) {
        try {
            // save file to PostgreSQL
            Item itemsito = itemRepository.findItemById(itemId);
            File filemode = new File();
            //filemode.setItem(itemsito);
            filemode.setItem(itemsito);
            filemode.setMimetype(file.getContentType());
            filemode.setName(file.getOriginalFilename());
            filemode.setPic(file.getBytes());
            fileRepository.save(filemode);
            return "File uploaded successfully! -> filename = " + file.getOriginalFilename();
        } catch (	Exception e) {
            return "FAIL! Maybe You had uploaded the file before or the file's size > 500KB";
        }





    }

    //method return list of file(files all) for item_id

    @JsonView(View.FileInfo.class)
    @GetMapping("/file/{itemId}/all")
    public List<File> getListFiles(@PathVariable (value = "itemId") Long itemId) {
        return fileRepository.findByItemId(itemId);
    }

    //method for download file with id
    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);

        if(fileOptional.isPresent()) {
            File file = fileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getPic());
        }

        return ResponseEntity.status(404).body(null);
    }



}
