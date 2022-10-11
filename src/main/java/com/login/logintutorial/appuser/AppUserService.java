package com.login.logintutorial.appuser;

import com.login.logintutorial.registration.token.ConfirmationToken;
import com.login.logintutorial.registration.token.ConfirmationTokenRepository;
import com.login.logintutorial.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {


    private final static String USER_NOT_FOUND_MSG = "User with email % not found";
    private final AppUserRepo appUserRepo;

    private  final ConfirmationToken confirmationToken;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExists = appUserRepo.findByEmail(appUser.getEmail()).isPresent();
        if(userExists){

            throw new IllegalStateException("Email already exists");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepo.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //TODO: send email
        return token;
    }

    public int enableAppUser(String email){
        return appUserRepo.enableAppUser(email);
    }


    public void deleteUser(String email){


        AppUser user = appUserRepo.findByEmail(email).orElseThrow();
        //code for user != null
        if(user.isEnabled()){

        }
         //throw new IllegalStateException("User profile does not exist or is not confirmed");
        appUserRepo.delete(user);
        deleteMessage();
        //System.out.println(user);



    }

    public List<AppUser>getUsers(){
        return appUserRepo.findAll();
    }

    public String deleteMessage(){
        return "the user profile has been deleted "  ;
    }


    public AppUser updateAppUser(AppUser appUser){
        AppUser appUser1 = appUserRepo.findById(appUser.getId()).get();

        appUser1.setFirstName(appUser.getFirstName());
        appUser1.setLastName(appUser.getLastName());
        appUser1.setEmail(appUser.getEmail());
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser1.setPassword(encodedPassword);
        
        return appUserRepo.save(appUser);
    }



    }
