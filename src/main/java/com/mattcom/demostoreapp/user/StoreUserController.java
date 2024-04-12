package com.mattcom.demostoreapp.user;


import com.mattcom.demostoreapp.order.exception.AddressNotFoundException;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.user.address.Address;
import com.mattcom.demostoreapp.user.address.AddressRepository;
import com.mattcom.demostoreapp.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void addUser(@RequestBody StoreUserRequest userRequest) {
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
    public ResponseEntity<List<StoreUser>> getUsers() {
        return ResponseEntity.ok(storeUserService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreUser> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(storeUserService.getUser(id));
    }

    //Todo remove comments

    @GetMapping("/user")
    public ResponseEntity<StoreUser> getUser(@AuthenticationPrincipal StoreUser user) {
        return ResponseEntity.ok(storeUserService.getUser(user.getId()));
    }

    @GetMapping("/user/address")
    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal StoreUser user) {
        return ResponseEntity.ok(addressRepository.findByUser_Id(user.getId()));
    }


    @GetMapping("/{id}/address")
    public ResponseEntity<List<Address>> getAddresses(@PathVariable Integer id) {
        return ResponseEntity.ok(addressRepository.findByUser_Id(id));
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<Address> getAddresses(@PathVariable Integer id, @RequestBody Address address) {
        Optional<StoreUser> userOpt = storeUserRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with id " + id);
        }
        StoreUser refUser = new StoreUser();
        refUser.setId(id);
        address.setUser(refUser);
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @PutMapping("/{id}/address/{addressId}")
    public ResponseEntity<Address> updateAddress(@RequestBody Address address, @PathVariable Integer id, @PathVariable Integer addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            throw new AddressNotFoundException("Address not found with id " + addressId);
        }
        Address origAddress = addressOpt.get();
        address.setUser(origAddress.getUser());
        address.setId(origAddress.getId());
        return ResponseEntity.ok(addressRepository.save(address));
    }

    @DeleteMapping("/{id}/address/{addressId}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Integer id, @PathVariable Integer addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            throw new AddressNotFoundException("Address not found with id " + addressId);
        }
        Address addressToDel = addressOpt.get();
        addressToDel.setUser(null);
        addressRepository.delete(addressToDel);
        return ResponseEntity.ok(addressToDel);
    }


}
