package com.login.logintutorial.registration;

import com.login.logintutorial.appuser.AppUser;
import com.login.logintutorial.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AppUserService appUserService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping("confirm")
    public String confirm(@RequestParam("token")String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping
    public List<AppUser>getAppUsers(){
        return appUserService.getUsers();
    }

    @DeleteMapping(path = "{email}")
    public void deleteUsr(@PathVariable("email") String email){
        //System.out.println(email);
        appUserService.deleteUser(email);
        System.out.println("user profile associated with username " + email + " has been deleted!");
    }

    @PutMapping
    public AppUser updateUser(@RequestBody AppUser appUser){
        return appUserService.updateAppUser(appUser);
    }

}
