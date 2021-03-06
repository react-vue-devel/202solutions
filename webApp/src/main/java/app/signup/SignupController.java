package app.signup;

import support.web.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by tanzeelrana on 3/25/2017.
 */

@Controller
public class SignupController {

    @ModelAttribute("module")
    String module() {
        return "signup";
    }

    private static final String SIGNUP_VIEW_NAME = "signup/signup";

    @GetMapping("signup")
    String signup(Principal principal, Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        if(principal != null){
            return "home/homeSignedIn";
        }

        model.addAttribute(new SignupForm());
        if (Ajax.isAjaxRequest(requestedWith)) {
            return SIGNUP_VIEW_NAME.concat(" :: signupForm");
        }
        return SIGNUP_VIEW_NAME;
    }

    @PostMapping("signup")
    String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }
//        Account account = accountService.save(signupForm.createAccount());
//        accountService.signin(account);
//        accountService.setCurrentUser(account);
        MessageHelper.addSuccessAttribute(ra, "signup.success");
        return "redirect:/";
        //return "/about";
    }

}
