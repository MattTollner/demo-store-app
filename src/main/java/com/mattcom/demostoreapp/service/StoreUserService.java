package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.entity.Address;
import com.mattcom.demostoreapp.entity.Product;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class StoreUserService {

    private StoreUserRepository storeUserRepository;

    public StoreUserService(StoreUserRepository storeUserRepository) {
        this.storeUserRepository = storeUserRepository;
    }

    public List<StoreUser> getUsers(){
        return storeUserRepository.getUsersRolesAndAddresses();
    }

    public StoreUser getUser(int id){
        return storeUserRepository.getUsersRolesAndAddressesById(id);
    }

    public StoreUser createUser(StoreUserRequest storeUserRequest){
        StoreUser user = new StoreUser();
        user.setCreateTime(LocalDate.now());
        user.setEmail(storeUserRequest.getEmail());
        user.setFirstName(storeUserRequest.getFirstName());
        user.setLastName(storeUserRequest.getLastName());
        user.setPhoneNumber(storeUserRequest.getPhoneNumber());
        user.setPassword(storeUserRequest.getPassword());
        user.setRoles(storeUserRequest.getRoles());
        return storeUserRepository.save(user);
    }

    public StoreUser updateUser(StoreUserRequest userRequest) throws Exception {
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
        user.setRoles(userRequest.getRoles());


        return storeUserRepository.save(user);
    }

    public void deleteUser(Integer productId) throws Exception {
        Optional<StoreUser> user = storeUserRepository.findById(productId);
        if (!user.isPresent()) {
            throw new Exception("User not found");
        }
        StoreUser userToDel = user.get();
        userToDel.setRoles(new HashSet<>());
        storeUserRepository.delete(userToDel);
    }

}
