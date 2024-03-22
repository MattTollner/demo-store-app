package com.mattcom.demostoreapp.controller;


import com.mattcom.demostoreapp.dao.AddressRepository;
import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.entity.Address;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.service.StoreUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class StoreUserController {

     StoreUserService storeUserService;
     AddressRepository addressRepository;
    private final StoreUserRepository storeUserRepository;

    public StoreUserController(StoreUserService storeUserService, AddressRepository addressRepository,
                               StoreUserRepository storeUserRepository) {
        this.storeUserService = storeUserService;
        this.addressRepository = addressRepository;
        this.storeUserRepository = storeUserRepository;
    }

    @PostMapping("/create")
    public void addUser(@RequestBody StoreUserRequest userRequest) throws Exception {
        storeUserService.createUser(userRequest);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody StoreUserRequest userRequest) throws Exception {
        storeUserService.updateUser(userRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") Integer id) throws Exception {
        storeUserService.deleteUser(id);
    }

    @GetMapping()
    public ResponseEntity<List<StoreUser>> getUsers(){
        return ResponseEntity.ok(storeUserService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreUser> getUser(@PathVariable Integer id){
        return ResponseEntity.ok(storeUserService.getUser(id));
    }


    @GetMapping("/{id}/address")
    public ResponseEntity<List<Address>> getAddresses(@PathVariable Integer id){
        return ResponseEntity.ok(addressRepository.findByUser_Id(id));
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<Address> getAddresses(@PathVariable Integer id, @RequestBody Address address){
        Optional<StoreUser> userOpt = storeUserRepository.findById(id);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        StoreUser storeUser = userOpt.get();
        StoreUser refUser = new StoreUser();
        refUser.setId(id);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @PutMapping("/{id}/address/{addressId}")
    public ResponseEntity<Address> updateAddress(@RequestBody Address address, @PathVariable Integer id, @PathVariable Integer addressId){
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (!addressOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Address origAddress = addressOpt.get();
        address.setUser(origAddress.getUser());
        address.setId(origAddress.getId());
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @DeleteMapping("/{id}/address/{addressId}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Integer id, @PathVariable Integer addressId) throws Exception {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (!addressOpt.isPresent()) {
            throw new Exception("Could not find address");
        }
        Address addressToDel = addressOpt.get();
        addressToDel.setUser(null);
        addressRepository.delete(addressToDel);
        return ResponseEntity.ok(addressToDel);
    }



}
