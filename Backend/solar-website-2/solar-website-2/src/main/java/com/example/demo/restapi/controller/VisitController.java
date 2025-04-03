package com.example.demo.restapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.restapi.entity.Visit;
import com.example.demo.restapi.service.PdfGenerationService;
import com.example.demo.restapi.service.VisitService;

@RestController
@RequestMapping("api/visits")
@CrossOrigin("http://localhost:4200")
public class VisitController {

    @Autowired
    private VisitService visitService; 
    
    @Autowired
    private PdfGenerationService pdfGenerationService;
    
    @PostMapping
    public ResponseEntity<Visit> createOrUpdateCustomer(@RequestBody Visit visitData) {
    	System.out.println("Testing");
        Visit savedVisitData = visitService.saveVisitData(visitData);
        return new ResponseEntity<>(savedVisitData, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateCustomer(@PathVariable("id") Long id, @RequestBody Visit visitData) {
        Visit updatedVsitData = visitService.updateCustomer(id, visitData);
        return updatedVsitData != null ? new ResponseEntity<>(updatedVsitData, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping
    public ResponseEntity<List<Visit>> getAllVisits() {
        List<Visit> visits = visitService.getAllVisits();
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<Visit> updateSubcdStatus(@RequestParam Long visitId, @RequestParam String status) {
        Visit updatedVisit = visitService.updateVisitStatus(visitId, status, "subcd");
        if (updatedVisit != null) {
            return ResponseEntity.ok(updatedVisit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Visit>> getCustomerByName(@PathVariable String name) {
        List<Visit> Visits= visitService.findCustomerByName(name);
        if (Visits.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Visits, HttpStatus.OK);
    }

    // Get customer by number
    @GetMapping("/number/{number}")
    public ResponseEntity<List<Visit>> getCustomerByNumber(@PathVariable String number) {
        List<Visit> customers = visitService.findCustomerByNumber(number);
        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }



    @PostMapping( "/installationStatus")
    public ResponseEntity<Visit> addVisit(@RequestParam Long visitId,@RequestParam String status,@RequestParam String type) {
        Visit savedVisit = visitService.updateVisitStatus(visitId,status,"installation");
        return new ResponseEntity<>(savedVisit, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadVisitPdf(@PathVariable Long id) {
        Optional<Visit> visitOpt = visitService.getVisitById(id);
        if (visitOpt.isPresent()) {
            Visit visit = visitOpt.get();
            try {
               
                byte[] pdfData = pdfGenerationService.generateVisitPdf(visit);

                
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=visit_report_" + id + ".pdf");

                
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)  // This sets the MIME type to application/pdf
                        .body(pdfData);
            } catch (Exception e) {
                
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
//    
//    @GetMapping("/download")
//    public ResponseEntity<byte[]> downloadVisitListPdf() {
//        List<Visit> visits = visitService.getAllVisits(); 
//
//        try {
//            byte[] pdfData = pdfGenerationService.generateVisitListPdf(visits);
//
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=visit_list.pdf");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.APPLICATION_PDF) 
//                    .body(pdfData);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    
//    
//    
}
