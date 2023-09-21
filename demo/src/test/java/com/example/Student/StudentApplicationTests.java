package com.example.Student;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentsApplicationTests {
// 	@LocalServerPort
//   int localServerPort;

// 	@Value(value = "${spring.data.rest.base-path}")   // nice trick to get basePath from application.properties
// 	String basePath;
	@Autowired
    TestRestTemplate restTemplate;

	@Test
	void shouldReturnFilenameNotSpecified() {
        ResponseEntity<Object> response = restTemplate
                .getForEntity("/api/students", Object.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        try{
            Map<String, Object> map = (Map<String, Object>)response.getBody();
            Assert.isTrue(((String)map.get("error")).equals("fileName not specified"), "Expected: fileName not found error");
        }catch(Exception e){
            Assert.isTrue(false, "Exception caused while running 'shouldReturnFilenameNotSpecified'");
        }  
    }



    @Test
    void shouldReturnNoFile() {
        //String[] arr = {"student", "dept", "college"};
        ArrayList<String> ss = new ArrayList<String>();
        ss.add("students");
        ss.add("dept");
        ss.add("college");
        for(String s: ss){
            ResponseEntity<Object> response = restTemplate
                    .getForEntity("/api/"+s+"/ThisIsNotAValidFileName", Object.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

            try{
                Map<String, Object> map = (Map<String, Object>)response.getBody();
                Assert.isTrue(((String)map.get("error")).equals("no csv file named ThisIsNotAValidFileName.csv"), "Expected: fileName not found error");
            }catch(Exception e){
                Assert.isTrue(false, "Exception caused while running 'shouldReturnNoFile'");
            }  
        }
    } 

    @Test
	void shouldReturnAllStudents() {
        ArrayList<String> ss = new ArrayList<String>();
        ss.add("students");
        ss.add("dept");
        ss.add("college");
        for(String s: ss){
            ResponseEntity<Object> response = restTemplate
                    .getForEntity("/api/"+s+"/test", Object.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); 
        } 
    }
    


 
}
