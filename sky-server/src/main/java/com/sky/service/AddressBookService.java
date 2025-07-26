package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void addAddress(AddressBook addressBook);

    List<AddressBook> list(AddressBook addressBook);


    void updateAddress(AddressBook addressBook);

    void deleteById(Integer id);

    AddressBook getAddressById(Integer id);

    void setDefaultAddress(AddressBook addressBook);
}
