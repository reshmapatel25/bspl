package com.example.register.repository;

import com.example.register.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    VerificationCode findByToken(String token);
    VerificationCode findByOtp(String otp);
}
