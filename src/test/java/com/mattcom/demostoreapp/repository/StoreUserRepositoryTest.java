package com.mattcom.demostoreapp.repository;

import com.mattcom.demostoreapp.user.StoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StoreUserRepositoryTest {

    @Autowired
    private StoreUserRepository storeUserRepository;

//    @Test
//    public void StoreUserRepository_save_returnSavedStoreUser() {
//        StoreUser storeUser = new StoreUser();
//        storeUser.setEmail("testsave@example.com");
//        storeUser.setFirstName("Matt");
//        storeUser.setLastName("Combs");
//        storeUser.setPassword("password");
//        storeUser.setPhoneNumber("0123456789");
//
//        StoreUser savedStoreUser = storeUserRepository.save(storeUser);
//
//
//    }
}
