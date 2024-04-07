package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.api.model.LoginInfo;
import com.mattcom.demostoreapp.api.model.PasswordResetInfo;
import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.dao.VerificationTokenRepository;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.entity.VerificationToken;
import com.mattcom.demostoreapp.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.exception.UserNotVerifiedException;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class StoreUserService {

    private StoreUserRepository storeUserRepository;
    private EncryptionService encryptionService;
    private JWTService jwtService;
    private EmailService emailService;
    private VerificationTokenRepository verificationTokenRepository;

    public StoreUserService(StoreUserRepository storeUserRepository, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, VerificationTokenRepository verificationTokenRepository) {
        this.storeUserRepository = storeUserRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public StoreUser registerUser(StoreUserRequest registrationInfo) throws StoreUserExistsException, FailureToSendEmailException {
        if (storeUserRepository.findByEmailIgnoreCase(registrationInfo.getEmail()).isPresent()) {
            throw new StoreUserExistsException();
        }
        StoreUser user = new StoreUser();
        user.setEmail(registrationInfo.getEmail());
        user.setFirstName(registrationInfo.getFirstName());
        user.setLastName(registrationInfo.getLastName());
        user.setPassword(encryptionService.encryptPassword(registrationInfo.getPassword()));
        //user.setRoles(registrationInfo.getRoles());
        VerificationToken token = createConfirmationToken(user);
        emailService.sendVerificationToken(token);
        // verificationTokenRepository.save(token);
        return storeUserRepository.save(user);
    }

    private VerificationToken createConfirmationToken(StoreUser storeUser) {
        VerificationToken token = new VerificationToken();
        token.setToken(jwtService.generateVerificationJWT(storeUser));
        token.setStoreUser(storeUser);
        token.setCreated(new Timestamp(System.currentTimeMillis()));
        storeUser.getVerificationTokens().add(token);
        return token;
    }

    public String loginUser(LoginInfo loginInfo) throws FailureToSendEmailException, UserNotVerifiedException {
        Optional<StoreUser> userOptional = storeUserRepository.findByEmailIgnoreCase(loginInfo.getEmail());
        if (userOptional.isEmpty()) {
            return null;
        }

        StoreUser storeUser = userOptional.get();
        if (encryptionService.verifyPassword(loginInfo.getPassword(), storeUser.getPassword())) {
            if (storeUser.isEmailVerified()) {
                return jwtService.gererateJWT(storeUser);
            } else {
                Set<VerificationToken> tokens = storeUser.getVerificationTokens();
                boolean resend = tokens.isEmpty() ||
                        tokens.iterator().next().getCreated().before(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));
                if (resend) {
                    VerificationToken token = createConfirmationToken(storeUser);
                    verificationTokenRepository.save(token);
                    emailService.sendVerificationToken(token);
                }
                throw new UserNotVerifiedException(resend);
            }

        }

        return null;
    }

    @Transactional
    public boolean verifyUser(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            return false;
        }
        StoreUser user = verificationToken.get().getStoreUser();
        if(!user.isEmailVerified()){
            user.setEmailVerified(true);
            storeUserRepository.save(user);
            long numOfDeleted = verificationTokenRepository.deleteByStoreUser(user);
            System.out.println("Number of deleted tokens " + numOfDeleted);
            return true;
        }

        return false;
    }



    public List<StoreUser> getUsers() {
        return storeUserRepository.getUsersRolesAndAddresses();
    }

    public StoreUser getUser(int id) {
        return storeUserRepository.getUsersRolesAndAddressesById(id);
    }

    public StoreUser createUser(StoreUserRequest storeUserRequest) {
        StoreUser user = new StoreUser();
        user.setEmail(storeUserRequest.getEmail());
        user.setFirstName(storeUserRequest.getFirstName());
        user.setLastName(storeUserRequest.getLastName());
        user.setPhoneNumber(storeUserRequest.getPhoneNumber());
        user.setPassword(storeUserRequest.getPassword());
        user.setRoles(storeUserRequest.getRoles());
        return storeUserRepository.save(user);
    }

    public StoreUser updateUser(@Valid StoreUserRequest userRequest) throws Exception {
        Optional<StoreUser> userOpt = storeUserRepository.findById(userRequest.getId());
        if (!userOpt.isPresent()) {
            throw new Exception("User not found");
        }

        StoreUser user = userOpt.get();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setPassword(userRequest.getPassword());
        //user.setRoles(userRequest.getRoles());


        return storeUserRepository.save(user);
    }

    public void deleteUser(Integer productId) throws Exception {
        Optional<StoreUser> user = storeUserRepository.findById(productId);
        if (!user.isPresent()) {
            throw new Exception("User not found");
        }
        StoreUser userToDel = user.get();
        //userToDel.setRoles(new HashSet<>());
        storeUserRepository.delete(userToDel);
    }

    public void forgotPassword(String email) throws FailureToSendEmailException, UsernameNotFoundException {
        Optional<StoreUser> user = storeUserRepository.findByEmailIgnoreCase(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        String token = jwtService.generateResetJWT(user.get());
        emailService.sendPasswordResetEmail(user.get(),token);
    }

    public void resetPassword(PasswordResetInfo passwordResetInfo) throws Exception {
        String email = jwtService.getEmailResetPassword(passwordResetInfo.getToken());
        Optional<StoreUser> userOpt = storeUserRepository.findByEmailIgnoreCase(email);
        if(userOpt.isEmpty()){
            throw new Exception("Token user does not exist");
        }
        StoreUser user = userOpt.get();
        user.setPassword(encryptionService.encryptPassword(passwordResetInfo.getPassword()));
        storeUserRepository.save(user);
    }

}
