package com.MDE_API;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.text.Document;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import Transfo1.Manip1;




/*
 * 
  @PostMapping(path = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @ResponseStatus(HttpStatus.CREATED)
  public Document uploadNewDocument(
      @AuthenticationPrincipal MyUserDetails userDetails,
      @RequestPart(name = "file", required = true) MultipartFile file,
      @Valid @RequestPart CreateDocumentDto data) {
    return documentsService.uploadDocumentForUser(userDetails.getUser(), file, data);
  }
 */
@RestController
public class TransfoController {

    //create a post request that tkaes a strig and returns the JenkinsFile of the project
    @PostMapping("/transform")
    public ResponseEntity<byte[]> transform(@RequestPart(name = "file", required = true) MultipartFile file) {
        try {

            String path = "initConfig.model";
 
            // // Get the original file name
            // String fileName = file.getOriginalFilename();

            // // Define the path where the file will be saved
            // String rootDirectory = System.getProperty("user.dir");
            // String fullPath = Paths.get(rootDirectory, fileName).toString();

            // Save the file to the root directory
            file.transferTo(Paths.get(path));

            
            System.out.println(path);
            Manip1 m = new Manip1();
            m.ExecManip1(path);
    
            // Read the content of the pre-existing Jenkinsfile
            String jenkinsfilePath = "JenkinsFile";
            Path pathh = Paths.get(jenkinsfilePath);
            byte[] jenkinsfileBytes = Files.readAllBytes(pathh);
    
            // Set up the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentLength(jenkinsfileBytes.length);
            headers.setContentDispositionFormData("attachment", "Jenkinsfile");
    
            // Return the response entity with the Jenkinsfile content
            return ResponseEntity.ok().headers(headers).body(jenkinsfileBytes);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
    
            // Include the exception details in the response body
            String errorMessage = "Internal Server Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage.getBytes());
        }
    }
    
}
