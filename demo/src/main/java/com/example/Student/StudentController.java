package com.example.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.util.UriComponentsBuilder;

// import jakarta.annotation.security.RolesAllowed;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.security.Principal;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentController {

    ResourceLoader resourceLoader;

    @Autowired
    public StudentController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello");
    }
    /*
     * id
     * name
     * dept
     * dept_head
     * college
     */

    static Student getStudent(String line) {
        Pattern pattern = Pattern.compile(",");
        String[] x = pattern.split(line);
        return new Student(Integer.parseInt(x[0]), x[1], x[2], x[3], x[4]);
    }

    List<Student> getOnlyStudents(BufferedReader in, List<String> dept, List<String> dept_head, List<String> college) throws IOException{

        List<Student> students = new LinkedList<Student>();
        String line = in.readLine();
        int indexs[] = {0,1,2,3,4};

        // assert false : "TODO: if the coloumns are shuffled this give wrong output \n"+
        //                 "If you are sure the the coloumns are not shuffled the comment this assert!";

        Pattern pattern = Pattern.compile(",");
        while((line = in.readLine()) != null){
            String[] x = pattern.split(line);
            if((dept==null || dept.contains(x[indexs[2]]))
                && (dept_head==null || dept_head.contains(x[indexs[3]]))
                && (college==null || college.contains(x[indexs[4]])))
            students.add(new Student(Integer.parseInt(x[indexs[0]]), x[indexs[1]], x[indexs[2]], x[indexs[3]], x[indexs[4]]));
        }
        return students;
    }
    
    @GetMapping("/students")
    public ResponseEntity<Map<String, Object>> sendError() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("error", "fileName not specified");
        return new ResponseEntity<Map<String,Object>>(map, null, 400);
    }

    // @GetMapping("/students/{fileName}")
    // public Map<String, Object> sendAll(@PathVariable("fileName") String fileName) {
        
    //     HashMap<String, Object> map = new HashMap<>();

    //     try (BufferedReader in = new BufferedReader(
    //             new FileReader(resourceLoader.getResource("classpath:/"+fileName+".csv").getFile()))) {
    //         List<Student> students = in.lines().skip(1).map(StudentController::getStudent).collect(Collectors.toList());
    //         map.put("data", students);
    //     } catch (FileNotFoundException fne) {
    //         map.put("error", "no csv file named " + fileName + ".csv");
    //     } catch (IOException ie) {
    //         map.put("error", "io error occured while reading the file");
    //     } catch (Exception e) {
    //         map.put("error", "exception occured while reading the file(file format incorrect)");
    //     }
    //     return map;
    // }

    @GetMapping("/students/{fileName}")
    public ResponseEntity<Map<String, Object>> sendOnly(@PathVariable("fileName") String fileName, @RequestParam(required = false) List<String> dept, @RequestParam(required = false) List<String> dept_head, @RequestParam(required = false) List<String> college) {
        
        HashMap<String, Object> map = new HashMap<>();
        int status = 500;

        try (BufferedReader in = new BufferedReader(
                new FileReader(resourceLoader.getResource("classpath:/"+fileName+".csv").getFile()))) {
            List<Student> students = getOnlyStudents(in, dept, dept_head, college);
            map.put("data", students);
            status = 200;
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            map.put("error", "no csv file named " + fileName + ".csv");
            status = 400;
        } catch (IOException ie) {
            ie.printStackTrace();
            map.put("error", "io error occured while reading the file");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "exception occured while reading the file(file format incorrect)");
        }
        return new ResponseEntity<Map<String,Object>>(map, null, status);
    }


    @GetMapping("/dept/{fileName}")
    public ResponseEntity<Map<String, Object>> sendDeptOnly( @PathVariable("fileName") String fileName, @RequestParam(required = false) List<String> dept, @RequestParam(required = false) List<String> dept_head, @RequestParam(required = false) List<String> college) {
        
        HashMap<String, Object> map = new HashMap<>();
        int status = 500;

        try (BufferedReader in = new BufferedReader(
                new FileReader(resourceLoader.getResource("classpath:/"+fileName+".csv").getFile()))) {
            List<Student> students = getOnlyStudents(in, dept, dept_head, college);
            
            HashMap<String, List<Student>> depts = new HashMap<String, List<Student>>();

            for(Iterator<Student> itr = students.iterator();itr.hasNext();){
                Student cStudent = itr.next();
                String cdept = cStudent.getDept();
                if(!depts.containsKey(cdept)){
                    depts.put(cdept, new LinkedList<Student>());
                }
                depts.get(cdept).add(cStudent);
                itr.remove();
            }
            map.put("data", depts);
            status = 200;
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            map.put("error", "no csv file named " + fileName + ".csv");
            status = 400;
        } catch (IOException ie) {
            ie.printStackTrace();
            map.put("error", "io error occured while reading the file");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "exception occured while reading the file(file format incorrect)");
        }
        return new ResponseEntity<Map<String,Object>>(map, null, status);
    }

    @GetMapping("/college/{fileName}")
    public ResponseEntity<Map<String, Object>> sendCollegeOnly( @PathVariable("fileName") String fileName, @RequestParam(required = false) List<String> dept, @RequestParam(required = false) List<String> dept_head, @RequestParam(required = false) List<String> college) {
        
        HashMap<String, Object> map = new HashMap<>();
        int status = 500;
        try (BufferedReader in = new BufferedReader(
                new FileReader(resourceLoader.getResource("classpath:/"+fileName+".csv").getFile()))) {
            List<Student> students = getOnlyStudents(in, dept, dept_head, college);
            
            HashMap<String, List<Student>> depts = new HashMap<String, List<Student>>();

            for(Iterator<Student> itr = students.iterator();itr.hasNext();){
                Student cStudent = itr.next();
                String cdept = cStudent.getCollege();
                if(!depts.containsKey(cdept)){
                    depts.put(cdept, new LinkedList<Student>());
                }
                depts.get(cdept).add(cStudent);
                itr.remove();
            }
            map.put("data", depts);
            status = 200;
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            map.put("error", "no csv file named " + fileName + ".csv");
            status = 400;
        } catch (IOException ie) {
            ie.printStackTrace();
            map.put("error", "io error occured while reading the file");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error", "exception occured while reading the file(file format incorrect)");
        }

        return new ResponseEntity<Map<String,Object>>(map, null, status);
    }

}
