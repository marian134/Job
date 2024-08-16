package com.job.job.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.job.job.JobRepository.JobRepository;
import com.job.job.model.Job;
import com.job.job.model.JobDTO;
import com.job.job.model.JobDTOExterno;
import com.squareup.okhttp.OkHttpClient;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.*;

@Component
@Service
public class JobService {

@Autowired
private RestTemplate restTemplate;

   public List<JobDTOExterno> getJobsFromSource(String urls, String country) throws JsonProcessingException {
    List<JobDTOExterno> allJobs = new ArrayList<>();
     for (String url :urls.split(",")) {
//         String response = restTemplate.getForObject("http://localhost:8082/jobs/", java.lang.String.class);
     String response = restTemplate.getForObject("http://localhost:8082/jobs/", java.lang.String.class);
         List<JobDTOExterno> jobsFromSource = convertResponseToJobs(response);

         allJobs.addAll(jobsFromSource);
     }
         allJobs.sort((j1, j2) -> j1.getCreatedAt().compareTo(j2.getCreatedAt()));
         return allJobs;
   }
    public String getJobsFromSource1(String urls, String name, Integer salarioMin, Integer salarioMax, String pais ) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String params = "";
        if(name != null || salarioMin != null || salarioMax != null || pais != null) {
            params += "?";
        }
        if(name != null) {
            params += "name=" + name;
        }

        if(salarioMin != null) {
            if(!params.equalsIgnoreCase("")) { params += "&"; }
            params += "salary_min=" + salarioMin;
        }

        if(salarioMax != null) {
            if(!params.equalsIgnoreCase("")) { params += "&"; }
            params += "salary_max=" + salarioMax;
        }

        if(pais != null) {
            if(!params.equalsIgnoreCase("")) { params += "&"; }
            params += "country=" + pais;
        }

       Request request = new Request.Builder()
                .url("http://localhost:8082/jobs" + params)
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();
        System.out.println("Response: " + json);
        return json;
    }
    private List<JobDTOExterno> convertResponseToJobs(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JobDTOExterno[] jobsArray = mapper.readValue(response, JobDTOExterno[].class);
        return Arrays.asList(jobsArray);
       }


    @Autowired
    private JobRepository jobRepository;


    public Job createJob (JobDTO body){
    Job job = new Job();
    job.setTitle(body.getTitle());
    job.setCompany(body.getCompany());
    job.setDescription(body.getDescription());
    job.setLocation(body.getLocation());
        return jobRepository.save(job);
    }
    public Job getJobById(Long id){
        Optional<Job> optionalJob = jobRepository.findById(id);
        return optionalJob.get();
    }

    public List<Job> getAllJobs(){
        return jobRepository.findAll();
    }
    public void deleteJob(Long id){
        jobRepository.deleteById(id);
}

}

