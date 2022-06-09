package com.example.register.service;

import com.example.register.error.RecordAlreadyExistException;
import com.example.register.repository.UserRepository;
import com.example.register.repository.VerificationCodeRepository;
import com.example.register.model.User;
import com.example.register.model.VerificationCode;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserRegistrationService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    VerificationCodeRepository verificationCodeRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public String getThisUser(Principal principal) {
        return principal.getName();
    }

    public User registerUser(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
      try{
          if(checkIfUserExist(user.getEmail()) || checkIfUserExist(user.getMobile())){
              throw new RecordAlreadyExistException("User already exists for this email or mobile");
          }
         // User uEntity = new User();
         // BeanUtils.copyProperties(user, uEntity);

          user.setVerificationCode(generateVerificationCode(user));
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          User u1=userRepo.save(user);
          sendVerificationEmail(u1, siteURL);
          return u1;
      }catch(DataIntegrityViolationException  dive){
          throw new RecordAlreadyExistException();
      }
        //System.out.println(user);

    }

    private boolean checkIfUserExist(String username) {
        return userRepo.findByEmailOrMobile(username)!=null ? true : false;
    }

    private VerificationCode generateVerificationCode(User user) {
    VerificationCode verificationCode;
        verificationCode = new VerificationCode();
        // random token is generated
        String randomCode = RandomString.make(64);
        verificationCode.setToken(randomCode);

        // Otp generated
        String randomOtp = "" ;
        int otp =  (int)(Math.random()*9000)+1000;
        randomOtp=String.valueOf(otp);
        verificationCode.setOtp(randomOtp);
        System.out.print("otp="+ verificationCode.getOtp());

        //expiry date
        verificationCode.setExpiryDate(calculateExpiryDate(10));

        verificationCode.setUser(user);
        return verificationCode;
    }
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
            String toAddress = user.getEmail();
            String fromAddress = "rrpassignment@gmail.com";
            String senderName = "RRP";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "RRP ";
    try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getFirstName());
            String verifyURL = siteURL + "/verifyEmail?code=" + user.getVerificationCode().getToken();

            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);
            System.out.print(content);
            mailSender.send(message);

            }catch( MessagingException | UnsupportedEncodingException e) {
                System.out.print(e);
            }
        }
@Transactional
    public boolean verify(String token) {
        VerificationCode code = verificationCodeRepo.findByToken(token);
        if(!validateVerificationCode(code)){
            return false;
        }
        User user=code.getUser();
        if(user.isVerifiedMobile()){
            user.setEnabled(true);
            verificationCodeRepo.delete(code);
            verificationCodeRepo.flush();
        }
        user.setVerifiedEmail(true);

            userRepo.save(user);
            userRepo.flush();
            return true;
       }
    @Transactional
    public boolean verifyOtp(String otp) {
        VerificationCode code;
        code = verificationCodeRepo.findByOtp(otp);
        if(!validateVerificationCode(code)){
            return false;
        }
        User user=code.getUser();
        if(user.isVerifiedEmail()){
            user.setEnabled(true);
            verificationCodeRepo.delete(code);
            verificationCodeRepo.flush();
        }
        user.setVerifiedMobile(true);
        userRepo.save(user);
        userRepo.flush();
        return true;
    }
    private boolean validateVerificationCode(VerificationCode code){
        if(code ==null || (code!=null && code.getUser().isEnabled())){
            return false;
        }
        Calendar cal = Calendar.getInstance();
        if( ((code.getExpiryDate().getTime() )- (cal.getTime().getTime())) <= 0){
            return false;
        }
        return true;
    }


}
