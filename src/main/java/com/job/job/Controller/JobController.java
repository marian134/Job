package com.job.job.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.job.job.model.Job;
import com.job.job.model.JobDTO;
import com.job.job.model.JobDTOExterno;
import com.job.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/jobs")
public class JobController {



    @Autowired
    private final JobService jobService;
    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<?> getJobs(@RequestParam(required=false) String name, @RequestParam(required=false) Integer salarioMin, @RequestParam(required=false) Integer salarioMax, @RequestParam(required=false) String pais) throws IOException {
        String jobs = jobService.getJobsFromSource1("http://localhost:8082/jobs/", name, salarioMin,salarioMax, pais);
       return ResponseEntity.ok(jobs) ;
}


    //endpoint
    @PostMapping
    public Job createJob(@RequestBody JobDTO dto){
        return jobService.createJob(dto);
    }


    @GetMapping("/search")
    public List<Job> getAllJobs(){
        return jobService.getAllJobs();
    }
    @GetMapping("{id}")
    public Job searchJobById(@PathVariable("id") Long id){
        return jobService.getJobById(id);
    }
    @DeleteMapping ("{id}")
    public void deleteJobById(@PathVariable("id") Long id){
       jobService.deleteJob(id);
    }

}
