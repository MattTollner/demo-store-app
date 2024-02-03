package com.mattcom.demostoreapp.dao;

import com.mattcom.demostoreapp.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Integer> {

    List<Address> findByUser_Id(Integer id);
}
