package com.example.demo.restapi.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.restapi.entity.Visit;
import com.example.demo.restapi.repository.VisitRepository;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    public Visit saveVisitData(Visit visitData) {
    	return visitRepository.save(visitData);
    }
    
    public Visit updateCustomer(Long id, Visit visitData) {
        if (visitRepository.existsById(id)) {
        	visitData.setId(id);
            return visitRepository.save(visitData);
        } else {
            return null; // or throw an exception
        }
    }
    
    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public Optional<Visit> getVisitById(Long visitId) {
        return visitRepository.findById(visitId);
    }
    public List<Visit> findCustomerByName(String name) {
        return visitRepository.findByConsumerName(name);
    }

  
    public List<Visit> findCustomerByNumber(String number) {
        return visitRepository.findByConsumerNumber(number);
    }
    public Visit addVisit(Visit visit) {
        return visitRepository.save(visit);
    }


    public Visit updateVisitStatus(Long visitId, String status, String type) {
        Optional<Visit> visitOpt = visitRepository.findById(visitId);
        if (visitOpt.isPresent()) {
            Visit visit = visitOpt.get();
            if ("subcd".equalsIgnoreCase(type)) {
                visit.setSubcdStatus(status);
            } else if ("installation".equalsIgnoreCase(type)) {
                visit.setInstallationStatus(status);
            }
            return visitRepository.save(visit);
        }
        return null;
    }


    public void deleteVisit(Long id) {
        visitRepository.deleteById(id);
    }
}
