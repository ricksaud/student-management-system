package com.sms.studentmanagementsystem.config;

import com.sms.studentmanagementsystem.model.Grade;
import com.sms.studentmanagementsystem.model.Student;
import com.sms.studentmanagementsystem.model.User;
import com.sms.studentmanagementsystem.repository.GradeRepository;
import com.sms.studentmanagementsystem.repository.ModuleRepository;
import com.sms.studentmanagementsystem.repository.StudentRepository;
import com.sms.studentmanagementsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(UserRepository userRepo,
                               StudentRepository studentRepo,
                               ModuleRepository moduleRepo,
                               GradeRepository gradeRepo,
                               PasswordEncoder passwordEncoder) {

        return args -> {

            if (userRepo.count() == 0) {

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                userRepo.save(user);

                Student s1 = new Student("John Doe", "john@example.com");
                s1.setOwner(user);

                Student s2 = new Student("Jane Smith", "jane@example.com");
                s2.setOwner(user);

                Student s3 = new Student("Michael Brown", "michael@example.com");
                s3.setOwner(user);

                studentRepo.save(s1);
                studentRepo.save(s2);
                studentRepo.save(s3);

                com.sms.studentmanagementsystem.model.Module m1 =
                        new com.sms.studentmanagementsystem.model.Module("Mathematics", "MATH101");
                m1.setOwner(user);

                com.sms.studentmanagementsystem.model.Module m2 =
                        new com.sms.studentmanagementsystem.model.Module("Computer Science", "CS102");
                m2.setOwner(user);

                com.sms.studentmanagementsystem.model.Module m3 =
                        new com.sms.studentmanagementsystem.model.Module("Physics", "PHY103");
                m3.setOwner(user);

                moduleRepo.save(m1);
                moduleRepo.save(m2);
                moduleRepo.save(m3);

                Grade g1 = new Grade();
                g1.setStudent(s1);
                g1.setModule(m1);
                g1.setScore(85.0);
                g1.setOwner(user);

                Grade g2 = new Grade();
                g2.setStudent(s2);
                g2.setModule(m2);
                g2.setScore(72.5);
                g2.setOwner(user);

                Grade g3 = new Grade();
                g3.setStudent(s3);
                g3.setModule(m3);
                g3.setScore(38.0);
                g3.setOwner(user);

                gradeRepo.save(g1);
                gradeRepo.save(g2);
                gradeRepo.save(g3);

                System.out.println("Test data loaded successfully.");
            }
        };
    }
}