package com.codingshuttle.youtube.hospitalManagement.Security;


import com.codingshuttle.youtube.hospitalManagement.dto.LoginRequestDto;
import com.codingshuttle.youtube.hospitalManagement.dto.LoginResponseDto;
import com.codingshuttle.youtube.hospitalManagement.dto.SignupResponseDto;
import com.codingshuttle.youtube.hospitalManagement.entity.User;
import com.codingshuttle.youtube.hospitalManagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;




    public SignupResponseDto signup(LoginRequestDto loginRequestDto) {
        User user = userRepo.findByUsername(loginRequestDto.getUsername()).orElse(null);
        if (user != null) {
            throw new IllegalArgumentException("Username already taken: " + loginRequestDto.getUsername());
        }
        user = userRepo.save(User.builder()
                .username(loginRequestDto.getUsername())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .build()
        );
        return new SignupResponseDto(user.getId(), user.getUsername());
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
                loginRequestDto.getPassword()));

        User user = (User) authenticate.getPrincipal();
        String token= authUtil.generateToken(user.getUsername());
        return new LoginResponseDto(token,user.getUsername());

    }
}
