package com.capgemini.UniversityCourseSelection.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.UniversityCourseSelection.entities.Admission;
import com.capgemini.UniversityCourseSelection.services.IAdmissionService;

@RestController
@RequestMapping("/admission")
public class AdmissionController {
	
	@Autowired
	public IAdmissionService service;
	
	
	@PostMapping("/addAdmission")
	public ResponseEntity<Admission> addAdmission(@RequestBody Admission admission) {
		Admission ref = service.addAdmission(admission);
		return new ResponseEntity<>(ref,HttpStatus.OK);	
	}
	
	
	@PutMapping("/updateAdmission")
    public ResponseEntity<Admission> updateAdmission(@RequestBody Admission admission){
		Admission ref = service.updateAdmission(admission);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	} 
    
	
	@DeleteMapping("/cancelAdmission/{admissionId}")
    public ResponseEntity<Admission> cancelAdmission(@PathVariable int admissionId){
		Admission ref = service.cancelAdmission(admissionId);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	} 
    
	
	@GetMapping("/alladmissionbyId/{courseId}")
    public ResponseEntity<List<Admission>> showAllAdmissionByCourseId(@PathVariable int courseId ){
		List<Admission> ref = service.showAllAdmissionByCourseId(courseId);
		return new ResponseEntity<>(ref,HttpStatus.OK);
	} 
    
	
	
//	@GetMapping("/alladmissionbyDate/{date}")
//    public ResponseEntity<List<Admission>> showAllAdmissionByDate(@RequestParam("localDate") @DateTimeFormat(pattern = "DD.MMM.YYYY") LocalDate date){
////		DateTimeFormatter dTF = new DateTimeFormatterBuilder().parseCaseInsensitive()
////	            .appendPattern("dd-MMM-yyyy")
////	            .toFormatter();
////		LocalDate date2  = LocalDate.parse(date.toString(),dTF);
////		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
////		LocalDate date2 = LocalDate.parse(format2.format(date));
//		List<Admission> ref = service.showAllAdmissionbyDate(date);
//		return new ResponseEntity<>(ref, HttpStatus.OK);
//	} 
	
	@GetMapping("/alladmissionbyDate/{date}/{month}/{year}")
    public ResponseEntity<List<Admission>> showAllAdmissionByDate(@PathVariable int date,@PathVariable String month,@PathVariable int year){
		DateTimeFormatter dTF = new DateTimeFormatterBuilder().parseCaseInsensitive()
	            .appendPattern("dd-MMM-yyyy")
	            .toFormatter();
		String datestring  = String.valueOf(date)+"-"+month+"-"+String.valueOf(year);
		LocalDate localdate  = LocalDate.parse(datestring,dTF);
		List<Admission> ref = service.showAllAdmissionbyDate(localdate);
		return new ResponseEntity<>(ref, HttpStatus.OK);
	} 
    
}


